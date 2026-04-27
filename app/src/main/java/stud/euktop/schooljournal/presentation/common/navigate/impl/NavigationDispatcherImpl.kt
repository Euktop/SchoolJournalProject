package stud.euktop.schooljournal.presentation.common.navigate.impl

import androidx.navigation.NavController
import stud.euktop.schooljournal.presentation.common.navigate.NavigationCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationDispatcher

class NavigationDispatcherImpl: NavigationDispatcher {
    override fun registerTabNavController(
        tabId: String,
        navController: NavController
    ) {
        TODO("Not yet implemented")
    }

    override fun unregisterTabNavController(tabId: String) {
        TODO("Not yet implemented")
    }

    override fun navigate(command: NavigationCommand) {
        TODO("Not yet implemented")
    }

    override fun back(): Boolean {
        TODO("Not yet implemented")
    }
}