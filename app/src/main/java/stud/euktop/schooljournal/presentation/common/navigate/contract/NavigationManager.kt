package stud.euktop.schooljournal.presentation.common.navigate.contract

import stud.euktop.schooljournal.presentation.common.navigate.Destination
import stud.euktop.schooljournal.presentation.common.navigate.Group
import stud.euktop.schooljournal.presentation.common.navigate.StackAction

interface NavigationManager {
    fun navigateTo(
        destination: Destination,
        stackAction: StackAction = StackAction.KEEP
    )

    fun navigateTo(
        group: Group,
        stackAction: StackAction = StackAction.KEEP
    )

    fun switchTab(
        destination: Destination,
        clearHistory: Boolean = false
    )

    fun back()
    fun backGroup()
    fun back(destination: Destination)
    fun backGroup(group: Group)
    fun onBackPressed(): Boolean
}