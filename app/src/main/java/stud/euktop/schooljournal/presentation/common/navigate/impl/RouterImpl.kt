@file:Suppress("DEPRECATION")

package stud.euktop.schooljournal.presentation.common.navigate.impl

import androidx.navigation.NavDirections
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.Nav1Directions
import stud.euktop.schooljournal.NavAdminDirections
import stud.euktop.schooljournal.NavAuthDirections
import stud.euktop.schooljournal.NavMainMainDirections
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdminHome
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuthorization
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterError
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMain
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMainMenu
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterSplash
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterStudent
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import stud.euktop.schooljournal.NavMainMainDirections as NMD

@Singleton
class RouterImpl @Inject constructor(
    private val navigationManager: NavigationManager, private val authRepository: AuthRepository
) : RouterSplash, RouterMain, RouterAuthorization, RouterAdmin, RouterError, RouterMainMenu,
    RouterProfile, RouterStudent, RouterTeacher, RouterAdminHome {

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
            NavCommand.ToDestination(R.id.mainProfileFragment) // Переход внутри nav_auth
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
    override suspend fun toSuccessChangePassword() = toBack()
    override suspend fun toCancelChangePassword() = toBack()
    override suspend fun toAfterSelectRole() {
        toMain()
    }

    override suspend fun toCancelCreatePassword() {
        navigationManager.navigate(NavCommand.Back)
    }

    override fun toChangePassword() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalChangePassword()))

    override fun toLogout() {
        navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()))
    }

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
                classId, subjectId
            )
        )
    )


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

    override fun toAdminPanel() {

    }

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

    private fun nav(navDirections: NavDirections) {
        navigationManager.navigate(NavCommand.ToAction(navDirections))
    }

    override fun toTeacherHomeworkList() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherHomeworkList()))

    override fun toStudentHomeworkList() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentHomeworkList()))

    override fun toStudentSchedule() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSchedule()))

    override fun toLessonEdit() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalLessonEdit()))

    override fun toSelectRole() {
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalSelectRole()))
    }

    override fun toStudentHome() = nav(NMD.actionGlobalHomeStudent())

    override fun toStudentAdmin() = nav(NMD.actionGlobalHomeAdmin())

    override fun toStudentTeacher() = nav(NMD.actionGlobalHomeTeacher())

    override suspend fun toBack() = navigationManager.navigate(NavCommand.Back)

    override fun toDashboard() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToDashboard())
        )
    }

    override fun toSchoolsList() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToSchools())
        )
    }

    override fun toSchoolEdit(schoolId: Int) {

    }

    override fun toClassesList() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToClasses())
        )
    }

    override fun toSubjectsList() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToSubjects())
        )
    }

    override fun toRoomsList() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToRooms())
        )
    }

    override fun toAssignmentsList() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToAssignments())
        )
    }

    override fun toUsersList() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToUsers())
        )
    }

    override fun toAuditLog() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToAudit())
        )
    }

    override fun toSettings() {
        navigationManager.navigate(
            NavCommand.ToAction(NavAdminDirections.actionToSettings())
        )
    }

    override fun toEditAssignment(id: AssignmentId) {
        navigationManager.navigate(
            NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherAssignmentEdit(id))
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

    override fun toEditUser(userId: Int) {
        navigationManager.navigate(
            NavCommand.ToAction(NavMainMainDirections.actionGlobalUserEdit(userId))
        )
    }

    override fun toEditSchool(schoolId: Int) {
        // TODO: реализовать SchoolEditFragment
        Timber.d("Edit school $schoolId – not implemented")
    }

    override fun toProfile() {
        navigationManager.navigate(
            NavCommand.ToAction(NavMainMainDirections.actionGlobalProfile())
        )
    }

    // ========== RouterAdminHome (дополнительные методы) ==========
    override fun toGenerateReport() {
        // TODO: диалог или экран отчёта
    }

    override fun toInviteUsers() {
        // TODO: диалог приглашения
    }

    override fun toMaintenance() {
        // TODO: диалог обслуживания
    }
}