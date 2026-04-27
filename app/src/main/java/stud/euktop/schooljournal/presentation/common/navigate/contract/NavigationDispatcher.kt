package stud.euktop.schooljournal.presentation.common.navigate.contract

import androidx.navigation.NavController
import stud.euktop.schooljournal.presentation.common.navigate.NavigationCommand

interface NavigationDispatcher {
    fun registerTabNavController(tabId: String, navController: NavController)

    fun unregisterTabNavController(tabId: String)

    fun navigate(command: NavigationCommand)

    fun back(): Boolean
}