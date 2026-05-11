package stud.euktop.schooljournal.presentation.common.navigate.impl

import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.Nav1Directions
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuthorization
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMain
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterSplash
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouterImpl @Inject constructor(
    private val navigationManager: NavigationManager,
    private val authRepository: AuthRepository
) : RouterSplash, RouterMain, RouterAuthorization, RouterAdmin {

    override suspend fun navigateAfterSplash() {
        if (authRepository.getCurrentUser().isSuccess) {
            toMain()
        } else {
            toLogin()
        }
    }

    override suspend fun toMain() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalNavMainMain())
        )
    }

    override suspend fun toCreateProfile() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToDestination(R.id.profileFragment)
        )
    }

    override suspend fun toCreatePassword() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToDestination(R.id.createPasswordFragment)
        )
    }

    override suspend fun toLogin() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToDestination(R.id.loginFragment)
        )
    }

    override suspend fun toSuccessCreate() {
        toMain()
    }

    override suspend fun toCancelCreatePassword() {
        navigationManager.navigate(NavCommand.Back)
    }
}