package stud.euktop.schooljournal.presentation.common.navigate.impl

import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterSplash
import javax.inject.Inject

class RouterImpl @Inject constructor(
    private val navigationManager: NavigationManager
) : RouterSplash {
    override suspend fun navigateAction() {
        navigationManager.navigate(
            NavCommand.ToDestination(
                destId = R.id.nav_main_main,
                targetKey = null,
                popUpTo = R.id.splashFragment,
                inclusive = true
            )
        )
    }
}