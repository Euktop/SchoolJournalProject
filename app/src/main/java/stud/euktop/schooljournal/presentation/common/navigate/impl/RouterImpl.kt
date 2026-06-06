@file:Suppress("DEPRECATION")

package stud.euktop.schooljournal.presentation.common.navigate.impl

import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.Nav1Directions
import stud.euktop.schooljournal.NavAuthDirections
import stud.euktop.schooljournal.NavMainMainDirections
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuthorization
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterError
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMain
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMainMenu
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterSplash
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterStudent
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouterImpl @Inject constructor(
    private val navigationManager: NavigationManager,
    private val authRepository: AuthRepository
) : RouterSplash, RouterMain, RouterAuthorization, RouterAdmin, RouterError,
    RouterMainMenu, RouterProfile, RouterStudent, RouterTeacher {

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
            NavCommand.ToDestination(R.id.authProfileFragment) // Переход внутри nav_auth
        )
    }

    override suspend fun toCreatePassword() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToDestination(R.id.createPasswordFragment)
        )
    }

    override suspend fun toLogin() {
        // actionGlobalToOnboarding уже ведет в nav_auth, который стартует с loginFragment.
        // Дополнительный ToDestination(R.id.loginFragment) избыточен.
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding())
        )
    }

    override suspend fun toSuccessCreate() = toMain()
    override suspend fun toSuccessChangePassword() = navigateBack()
    override suspend fun toCancelChangePassword() = navigateBack()

    override suspend fun toCancelCreatePassword() {
        navigationManager.navigate(NavCommand.Back)
    }

    override fun toChangePassword() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalChangePassword()))

    override fun toStudentSubjectDetail(subjectId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalStudentSubjectDetail(subjectId)
        )
    )

    override fun toTeacherHomeworkEdit() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherHomeworkEdit())) // homeworkId имеет defaultValue=0 в XML

    override fun toTeacherHomeworkEdit(homeworkId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalTeacherHomeworkEdit(homeworkId)
        )
    )

    override fun toTeacherLessons(classId: Int, subjectId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalTeacherLessons(
                classId,
                subjectId
            )
        )
    )


    override fun navigateBack() {
        navigationManager.navigate(NavCommand.Back)
    }

    // --- Переходы с аргументами теперь используют NavDirections ---

    override fun toEditUser(userId: Int) {
        navigationManager.navigate(
            NavCommand.ToAction(NavMainMainDirections.actionGlobalUserEdit(userId))
        )
    }

    override fun toEditClass(classId: Int) {
        navigationManager.navigate(
            NavCommand.ToAction(NavMainMainDirections.actionGlobalClassEdit(classId))
        )
    }

    override fun toEditSubject(subjectId: Int) {
        navigationManager.navigate(
            NavCommand.ToAction(NavMainMainDirections.actionGlobalSubjectEdit(subjectId))
        )
    }

    override fun toEditAssignment(assignmentId: AssignmentId) {
        navigationManager.navigate(
            NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherAssignmentEdit(assignmentId))
        )
    }

    // --- Обработка ошибок ---

    override fun onUnauthorized(): () -> Unit = {
        // Глобальный экшен action_global_to_onboarding в nav1 уже настроен на
        // переход к nav_auth с popUpTo="@id/splashFragment" и inclusive="true".
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding())
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
    override fun toTeacherClasses() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherClasses()))

    override fun toStudentSubjects() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSubjects()))

    override fun toAdminPanel() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalAdminPanel()))

    override fun toAuthProfile() =
        navigationManager.navigate(NavCommand.ToAction(NavAuthDirections.actionGlobalToAuthProfile()))

    override fun toStudentSubjectDetail() = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalStudentSubjectDetail()
        )
    ) // 0 - заглушка, если ID не важен

    override fun toNavAuth() =
        navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()))

    override fun toNavAuthWithProfile() = navigationManager.navigate(
        NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
        NavCommand.ToAction(NavAuthDirections.actionGlobalToAuthProfile())
    )

    override fun toNavAuthWithCreatePassword() = navigationManager.navigate(
        NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
        NavCommand.ToAction(NavAuthDirections.actionGlobalToCreatePassword())
    )

    override fun toLessonMarks(lessonId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalLessonMarks(lessonId)
        )
    )

    override fun toTeacherHomeworkList() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherHomeworkList()))

    override fun toStudentHomeworkList() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentHomeworkList()))

    override fun toStudentSchedule() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSchedule()))

    override fun toLessonEdit() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalLessonEdit()))


}