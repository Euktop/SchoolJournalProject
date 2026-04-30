package stud.euktop.schooljournal.presentation.common.navigate.impl

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.uikit.R
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManagerImpl @Inject constructor() : NavigationManager {

    private val controllers = mutableMapOf<String, NavController>()
    private val tasks = ArrayDeque<Pair<String?, NavCommand>>()
    private var mainNavController: NavController? = null

    // Анимации по умолчанию для переходов ToDestination
    private val defaultNavOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    override fun bindMain(navController: NavController) {
        mainNavController = navController
        controllers["main"] = navController
        processTasksFor("main")
    }

    override fun register(key: String, navController: NavController) {
        controllers[key] = navController
        processTasksFor(key)
    }

    override fun unregister(key: String) {
        controllers.remove(key)
    }

    override fun navigate(vararg cmds: NavCommand) {
        cmds.forEach { cmd ->
            logger?.let {
                val description = when (cmd) {
                    is NavCommand.ToDestination -> {
                        val name = resourceName(cmd.destId)
                        "ToDestination($name, args=${cmd.args})"
                    }

                    is NavCommand.Back -> "Back"
                    is NavCommand.ToAction -> cmd.directions::class.simpleName
                }
                it.d(toSimpleTag(), "Navigate", description)
            }
            val (nav, key) = when (cmd) {
                is NavCommand.ToDestination -> {
                    val targetKey = cmd.targetKey
                    val nav = if (targetKey != null) controllers[targetKey] else mainNavController
                    nav to targetKey
                }

                is NavCommand.ToAction,
                is NavCommand.Back -> {
                    val nav = controllers.values.lastOrNull() ?: mainNavController
                    nav to null
                }
            }
            if (nav != null)
                execute(nav, cmd)
            else
                tasks.addLast(Pair(key, cmd))
        }
    }

    private fun processTasksFor(key: String) {
        val nav = controllers[key] ?: return
        val iterator = tasks.iterator()
        while (iterator.hasNext()) {
            val (taskKey, cmd) = iterator.next()
            if (taskKey == key || (taskKey == null && nav == mainNavController)) {
                execute(nav, cmd)
                iterator.remove()
            }
        }
    }

    private fun execute(nav: NavController, command: NavCommand) {
        when (command) {
            is NavCommand.ToDestination -> {
                if (command.popUpTo != null) {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(command.popUpTo, command.inclusive)
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build()
                    nav.navigate(command.destId, command.args, navOptions)
                } else {
                    // Используем анимацию по умолчанию
                    nav.navigate(command.destId, command.args, defaultNavOptions)
                }
            }

            NavCommand.Back -> nav.popBackStack()
            is NavCommand.ToAction -> nav.navigate(command.directions)
        }
    }

    private fun resourceName(resId: Int): String {
        val context: Context? =
            mainNavController?.context ?: controllers.values.firstOrNull()?.context
        return try {
            context?.resources?.getResourceEntryName(resId) ?: resId.toString()
        } catch (_: Exception) {
            resId.toString()
        }
    }
}