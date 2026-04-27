package stud.euktop.schooljournal.presentation.common.navigate.contract

import androidx.navigation.NavController
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand

interface NavigationManager {
    fun navigate(cmd: NavCommand)
    fun bindMain(navController: NavController)
    fun register(key: String, navController: NavController)
    fun unregister(key: String)
}