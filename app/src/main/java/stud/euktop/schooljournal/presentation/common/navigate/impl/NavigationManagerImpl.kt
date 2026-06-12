@file:Suppress("DEPRECATION")

package stud.euktop.schooljournal.presentation.common.navigate.impl

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import kotlinx.coroutines.runBlocking
import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.state.NavigationStateManager
import stud.euktop.uikit.R
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManagerImpl @Inject constructor(
    private val stateManager: NavigationStateManager,
    private val roleRepository: RoleRepository,
) : NavigationManager {

    private val controllers = mutableMapOf<String, NavController>()
    private val tasks = ArrayDeque<Pair<String?, NavCommand>>()

    private val defaultNavOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    override fun bindMain(navController: NavController) {
        controllers["main"] = navController
        logger?.i(toSimpleTag(), "bindMain", "Главный NavController привязан")
        processTasksFor("main")
    }

    override fun register(key: String, navController: NavController) {
        controllers[key] = navController
        logger?.i(toSimpleTag(), "register", "Зарегистрирован контроллер по ключу '$key'")
        processTasksFor(key)
    }

    override fun unregister(key: String) {
        controllers.remove(key)
        logger?.i(toSimpleTag(), "unregister", "Удалён контроллер по ключу '$key'")
    }

    override fun navigate(vararg cmds: NavCommand) {
        cmds.forEach { cmd ->
            logger?.let { log ->
                val description = when (cmd) {
                    is NavCommand.ToDestination -> {
                        val name = resourceName(cmd.destId)
                        "ToDestination($name)"
                    }

                    is NavCommand.Back -> "Back"
                    is NavCommand.ToAction -> cmd.directions::class.simpleName
                    is NavCommand.PopUpTo -> "PopUpTo(${cmd.destinationId}, inclusive=${cmd.inclusive})"
                }
                log.d(toSimpleTag(), "Navigate", description)
            }

            val (nav, key) = when (cmd) {
                is NavCommand.ToDestination -> {
                    val targetKey = "main"
                    controllers[targetKey] to targetKey
                }

                is NavCommand.ToAction, is NavCommand.Back, is NavCommand.PopUpTo -> {
                    val nav = controllers.values.lastOrNull() ?: controllers["main"]
                    nav to null
                }
            }

            if (nav != null) {
                execute(nav, cmd)
            } else {
                tasks.addLast(Pair(key, cmd))
            }
        }
    }

    private fun processTasksFor(key: String) {
        val nav = controllers[key] ?: return
        val iterator = tasks.iterator()
        while (iterator.hasNext()) {
            val (taskKey, cmd) = iterator.next()
            if (taskKey == key || (taskKey == null && key == "main")) {
                execute(nav, cmd)
                iterator.remove()
                logger?.i(
                    toSimpleTag(),
                    "processTasksFor",
                    "Выполнена отложенная команда для ключа '$key'"
                )
            }
        }
    }

    private fun execute(nav: NavController, command: NavCommand) {
        try {
            when (command) {
                is NavCommand.ToDestination -> {
                    nav.navigate(command.destId, null, defaultNavOptions)
                    saveCurrentDestination(nav)
                    logger?.i(
                        toSimpleTag(),
                        "execute",
                        "Переход к destination id=${command.destId}"
                    )
                }

                NavCommand.Back -> {
                    val targetNav =
                        controllers.values.lastOrNull { it.previousBackStackEntry != null }
                            ?: controllers["main"]
                    val popped = targetNav?.popBackStack() ?: false
                    if (popped) {
                        saveCurrentDestination(targetNav)
                        logger?.i(toSimpleTag(), "execute", "Выполнен Back")
                    } else {
                        logger?.e(
                            toSimpleTag(),
                            "execute",
                            null,
                            "Back проигнорирован: достигнут корень графа"
                        )
                    }
                }

                is NavCommand.ToAction -> {
                    nav.navigate(command.directions)
                    saveCurrentDestination(nav)
                    logger?.i(
                        toSimpleTag(),
                        "execute",
                        "Выполнен ToAction: ${command.directions::class.simpleName}"
                    )
                }

                is NavCommand.PopUpTo -> {
                    nav.popBackStack(command.destinationId, command.inclusive)
                    saveCurrentDestination(nav)
                    logger?.i(
                        toSimpleTag(),
                        "execute",
                        "Выполнен PopUpTo до ${command.destinationId}"
                    )
                }
            }
        } catch (e: IllegalStateException) {
            Timber.e(e, "Navigation error: ${e.message}")
            logger?.e(toSimpleTag(), "execute", e, "Ошибка навигации для команды $command")
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Navigation argument error: ${e.message}")
            logger?.e(toSimpleTag(), "execute", e, "Неверные аргументы навигации для $command")
        }

        logBackStack(nav, command::class.simpleName ?: "Unknown")
    }

    private fun saveCurrentDestination(nav: NavController) {
        val destination = nav.currentBackStackEntry?.destination?.id
        val arguments = nav.currentBackStackEntry?.arguments
        if (destination != null) {
            runBlocking {
                val role = roleRepository.getCurrentRole()?.name ?: return@runBlocking
                stateManager.saveLastDestination(
                    role = role,
                    destinationData = NavigationStateManager.DestinationData(destination, arguments)
                )
            }
        }
    }

    private fun resourceName(resId: Int): String {
        val context = controllers["main"]?.context ?: controllers.values.firstOrNull()?.context
        return try {
            context?.resources?.getResourceEntryName(resId) ?: resId.toString()
        } catch (e: Exception) {
            logger?.e(
                toSimpleTag(),
                "resourceName",
                e,
                "Не удалось получить имя ресурса для ID=$resId"
            )
            resId.toString()
        }
    }

    private fun logBackStack(nav: NavController, action: String) {
        val currentEntry = nav.currentBackStackEntry
        val previousEntry = nav.previousBackStackEntry

        val graph = try {
            nav.graph
        } catch (e: IllegalStateException) {
            logger?.e(
                toSimpleTag(),
                "logBackStack",
                e,
                "Не удалось получить NavGraph из-за некорректного состояния навигатора"
            )
            null
        }

        fun getDestName(dest: NavDestination?): String {
            if (dest == null) return "null"
            return dest.route ?: dest.label?.toString() ?: try {
                nav.context.resources.getResourceEntryName(dest.id)
            } catch (e: Exception) {
                logger?.e(
                    toSimpleTag(), "getDestName", e, "Не удалось получить имя для destination"
                )
                "id=${dest.id}"
            }
        }

        val currentDest = currentEntry?.destination
        val previousDest = previousEntry?.destination

        val currentName = getDestName(currentDest)
        val previousName = getDestName(previousDest)
        val graphName = getDestName(graph)
        val navigatorName = currentDest?.navigatorName ?: "null"

        val arguments = currentEntry?.arguments?.let { bundle ->
            if (bundle.isEmpty) "нет аргументов"
            else bundle.keySet().joinToString(", ") { key -> "$key=${bundle.get(key)}" }
        } ?: "нет аргументов"

        fun getAllNodes(root: NavGraph?): List<String> {
            val nodes = mutableListOf<String>()
            if (root == null) return nodes
            nodes.add("Graph: ${getDestName(root)}")
            fun traverse(dest: NavDestination) {
                nodes.add(getDestName(dest))
                if (dest is NavGraph) {
                    dest.forEach { traverse(it) }
                }
            }
            root.forEach { traverse(it) }
            return nodes
        }

        val graphNodes = getAllNodes(graph)

        logger?.d(
            toSimpleTag(), "BackStack($action)", buildString {
                appendLine("=== Состояние навигации ===")
                appendLine("Текущий destination: $currentName")
                appendLine("Навигатор: $navigatorName")
                appendLine("Предыдущий destination: $previousName")
                appendLine("Корневой граф: $graphName")
                appendLine("Аргументы текущего: $arguments")
                appendLine("Всего узлов в графе: ${graphNodes.size}")
                appendLine("Список узлов: ${graphNodes.joinToString(", ")}")
                appendLine("===========================")
            })
    }
}