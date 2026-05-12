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
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterError
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMain
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterSplash
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouterImpl @Inject constructor(
    private val navigationManager: NavigationManager,
    private val authRepository: AuthRepository
) : RouterSplash, RouterMain, RouterAuthorization, RouterAdmin, RouterError {

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

    override suspend fun toSuccessChangePassword() {
        navigateBack()
    }

    override suspend fun toCancelChangePassword() {
        navigateBack()
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

    override fun onUnauthorized(): () -> Unit = {
        navigationManager.navigate(
            NavCommand.ToDestination(
                R.id.nav_auth,
                popUpTo = R.id.splashFragment,
                inclusive = true
            ),
            NavCommand.ToDestination(R.id.loginFragment)
        )
    }

    override fun onAccessDenied(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
    override fun onNetworkConnection(): () -> Unit = { /* просто показать сообщение */ }
    override fun onServerError(): () -> Unit = { /* показать сообщение */ }
    override fun onNotFound(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
    override fun onConflict(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
    override fun onSessionExpired(): () -> Unit = onUnauthorized()
    override fun onInvalidCredentials(): () -> Unit = { /* остаться на том же экране */ }
    override fun onUserBlocked(): () -> Unit = { /* остаться */ }
    override fun onUserDeleted(): () -> Unit = onUnauthorized()
    override fun onFieldRequired(): () -> Unit = { /* остаться */ }
    override fun onRecordNotFound(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
    override fun onUniqueViolation(): () -> Unit = { /* остаться */ }
    override fun onForeignKeyViolation(): () -> Unit =
        { navigationManager.navigate(NavCommand.Back) }

    override fun onRecordAlreadyFixed(): () -> Unit = { /* остаться */ }
    override fun onTeacherCannotTeachSubject(): () -> Unit = { /* остаться */ }
    override fun onStudentNotInClass(): () -> Unit = { /* остаться */ }
    override fun onNoPermissionToGrade(): () -> Unit = { /* остаться */ }
    override fun onNoPermissionToManageUser(): () -> Unit =
        { navigationManager.navigate(NavCommand.Back) }

    override fun onCannotDeleteClassWithStudents(): () -> Unit = { /* остаться */ }
    override fun onCannotDeleteClassWithLessons(): () -> Unit = { /* остаться */ }
    override fun onRoomNotBelongsToSchool(): () -> Unit = { /* остаться */ }
    override fun onTeacherTimeConflict(): () -> Unit = { /* остаться */ }
    override fun onClassTimeConflict(): () -> Unit = { /* остаться */ }
    override fun onStartTimeAfterEndTime(): () -> Unit = { /* остаться */ }
    override fun onCannotDeleteSchoolWithClasses(): () -> Unit = { /* остаться */ }
    override fun onCannotDeleteRoomUsedInLessons(): () -> Unit = { /* остаться */ }
    override fun onClassTimeOverlap(): () -> Unit = { /* остаться */ }
    override fun onMultiplePrimaryTeachers(): () -> Unit = { /* остаться */ }
    override fun onPermissionOutOfRange(): () -> Unit = { /* остаться */ }
    override fun onDefault(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
}