package stud.euktop.schooljournal.presentation.common.navigate.impl

import android.os.Bundle
import stud.euktop.domain.model.assignment.AssignmentId
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

    override fun navigateBack() {
        navigationManager.navigate(NavCommand.Back)
    }

    override fun toEditUser(userId: Int) {
        val bundle = Bundle().apply { putInt("userId", userId) }
        navigationManager.navigate(NavCommand.ToDestination(R.id.userEditFragment, bundle))
    }

    override fun toEditClass(classId: Int) {
        val bundle = Bundle().apply { putInt("classId", classId) }
        navigationManager.navigate(NavCommand.ToDestination(R.id.classEditFragment, bundle))
    }

    override fun toEditSubject(subjectId: Int) {
        val bundle = Bundle().apply { putInt("subjectId", subjectId) }
        navigationManager.navigate(NavCommand.ToDestination(R.id.subjectEditFragment, bundle))
    }

    override fun toEditAssignment(assignmentId: AssignmentId) {
        val bundle = Bundle().apply { putSerializable("assignmentId", assignmentId) }
        navigationManager.navigate(
            NavCommand.ToDestination(
                R.id.teacherAssignmentEditFragment,
                bundle
            )
        )
    }
}