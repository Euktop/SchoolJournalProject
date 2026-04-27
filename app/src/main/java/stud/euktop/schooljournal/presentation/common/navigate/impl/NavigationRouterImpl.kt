package stud.euktop.schooljournal.presentation.common.navigate.impl

import stud.euktop.schooljournal.presentation.common.navigate.Destination
import stud.euktop.schooljournal.presentation.common.navigate.StackAction
import stud.euktop.schooljournal.presentation.common.navigate.contract.FrameNavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationRouterSplash
import javax.inject.Inject

class NavigationRouterImpl @Inject constructor() : NavigationRouterSplash {

    override fun navigationAfterSplash(): (FrameNavigationManager) -> Unit {
        return { it.navigationManager.navigateTo(Destination.Login, StackAction.CLEAR_ALL) }
    }
}