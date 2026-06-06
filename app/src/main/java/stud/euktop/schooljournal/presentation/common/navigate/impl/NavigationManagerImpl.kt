package stud.euktop.schooljournal.presentation.common.navigate.impl

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.uikit.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация [NavigationManager] с поддержкой нескольких [NavController] и отложенных команд.
 *
 * @author 12345 (номер участника)
 * @since 15-05-2026
 */
@Singleton
class NavigationManagerImpl @Inject constructor() : NavigationManager {

    private val controllers = mutableMapOf<String, NavController>()
    private val tasks = ArrayDeque<Pair<String?, NavCommand>>()

    // Анимации по умолчанию для переходов ToDestination
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
            // Логирование отладочной информации о поступившей команде
            logger?.let {
                val description = when (cmd) {
                    is NavCommand.ToDestination -> {
                        val name = resourceName(cmd.destId)
                        "ToDestination($name)"
                    }
                    is NavCommand.Back -> "Back"
                    is NavCommand.ToAction -> cmd.directions::class.simpleName
                    is NavCommand.PopUpTo -> "PopUpTo(${cmd.destinationId}, inclusive=${cmd.inclusive})"
                }
                it.d(toSimpleTag(), "Navigate", description)
            }

            // Определяем, какой NavController должен обработать команду
            val (nav, key) = when (cmd) {
                is NavCommand.ToDestination -> {
                    // Переходы по ID destination всегда выполняются в главном контроллере
                    val targetKey = "main"
                    controllers[targetKey] to targetKey
                }
                // Для Back, ToAction и PopUpTo используем последний активный контроллер или главный
                is NavCommand.ToAction,
                is NavCommand.Back,
                is NavCommand.PopUpTo -> {
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

    /**
     * Обрабатывает все отложенные команды, ожидающие указанный ключ (или главный контроллер).
     */
    private fun processTasksFor(key: String) {
        val nav = controllers[key] ?: return
        val iterator = tasks.iterator()
        while (iterator.hasNext()) {
            val (taskKey, cmd) = iterator.next()
            if (taskKey == key || (taskKey == null && key == "main")) {
                execute(nav, cmd)
                iterator.remove()
                logger?.i(toSimpleTag(), "processTasksFor", "Выполнена отложенная команда для ключа '$key'")
            }
        }
    }

    /**
     * Непосредственное исполнение навигационной команды на указанном NavController.
     */
    private fun execute(nav: NavController, command: NavCommand) {
        when (command) {
            is NavCommand.ToDestination -> {
                // Используем defaultNavOptions, так как popUpTo и args теперь задаются через XML (ToAction)
                nav.navigate(command.destId, null, defaultNavOptions)
                logger?.i(toSimpleTag(), "execute", "Переход к destination id=${command.destId}")
            }
            NavCommand.Back -> {
                nav.popBackStack()
                logger?.i(toSimpleTag(), "execute", "Выполнен Back")
            }
            is NavCommand.ToAction -> {
                // NavDirections уже содержит все настройки (popUpTo, args, анимации) из XML
                nav.navigate(command.directions)
                logger?.i(toSimpleTag(), "execute", "Выполнен ToAction: ${command.directions::class.simpleName}")
            }
            is NavCommand.PopUpTo -> {
                // Очищаем back stack до указанного destination
                nav.popBackStack(command.destinationId, command.inclusive)
                logger?.i(toSimpleTag(), "execute", "Выполнен PopUpTo до ${command.destinationId}")
            }
        }
    }

    /**
     * Преобразует ресурс ID в имя ресурса для удобочитаемого лога.
     * В случае ошибки логирует исключение и возвращает просто ID.
     */
    private fun resourceName(resId: Int): String {
        val context = controllers["main"]?.context ?: controllers.values.firstOrNull()?.context
        return try {
            context?.resources?.getResourceEntryName(resId) ?: resId.toString()
        } catch (e: Exception) {
            logger?.e(toSimpleTag(), "resourceName", e, "Не удалось получить имя ресурса для ID=$resId")
            resId.toString()
        }
    }
}