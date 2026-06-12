@file:Suppress("DEPRECATION")

package stud.euktop.schooljournal.presentation.common.navigate.impl

import androidx.navigation.NavDirections
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.Nav1Directions
import stud.euktop.schooljournal.NavAdminDirections
import stud.euktop.schooljournal.NavAuthDirections
import stud.euktop.schooljournal.NavMainMainDirections
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuth
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
    private val navigationManager: NavigationManager,
    private val authRepository: AuthRepository
) : RouterSplash, RouterMain, RouterAuth, RouterAdmin, RouterError, RouterMainMenu,
    RouterProfile, RouterStudent, RouterTeacher {

    // --- RouterSplash & RouterMain ---
    override suspend fun navigateAfterSplash() {
        if (authRepository.getCurrentUser().isSuccess) toMain() else toLogin()
    }

    override suspend fun toMain() {
        navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalNavMainMain()))
    }

    // --- RouterAuthorization ---
    override suspend fun toCreateProfile() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToDestination(R.id.mainProfileFragment)
        )
    }

    override suspend fun toCreatePassword() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToDestination(R.id.createPasswordFragment)
        )
    }

    override suspend fun toLogin() {
        navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()))
    }

    override suspend fun toSuccessCreate() = toMain()
    override suspend fun toSuccessChangePassword() = toBack()
    override suspend fun toCancelChangePassword() = toBack()
    override suspend fun toAfterSelectRole() = toMain()
    override suspend fun toCancelCreatePassword() = navigationManager.navigate(NavCommand.Back)

    // --- RouterProfile ---
    override fun toProfile() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalProfile()))

    override fun toEditUser(userId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalUserEdit(userId)
        )
    )

    override fun toChangePassword() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalChangePassword()))

    override fun toLogout() =
        navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()))

    // --- RouterStudent ---
    override fun toStudentSubjectDetail(subjectId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalStudentSubjectDetail(subjectId)
        )
    )

    override fun toStudentSchedule() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSchedule()))

    override fun toStudentSubjects() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSubjects()))

    override fun toStudentMarks() { /* Экран еще не создан */
    }

    override fun toStudentHomework() { /* Экран еще не создан */
    }

    override fun toStudentAnalytics() { /* Экран еще не создан */
    }

    override fun toStudentProfile() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalProfile()))

    // --- RouterTeacher ---
    override fun toTeacherHomeworkEdit() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherHomeworkEdit()))

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

    override fun toTeacherLessonMarks(lessonId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalLessonMarks(lessonId)
        )
    )

    override fun toTeacherClasses() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherClasses()))

    override fun toTeacherHomeworkList() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherHomeworkList()))

    override fun toTeacherSchedule() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSchedule())) // Оставлена ваша логика навигации

    override fun toTeacherAnalytics() { /* Экран еще не создан */
    }

    // --- RouterMainMenu ---
    override fun toMainMenuTeacherClasses() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherClasses()))

    override fun toMainMenuStudentSubjects() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSubjects()))

    override fun toMainMenuAdminPanel() { /* TODO */
    }

    override fun toMainMenuAuthProfile() =
        navigationManager.navigate(NavCommand.ToAction(NavAuthDirections.actionGlobalToAuthProfile()))

    override fun toStudentSubjectDetail() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSubjectDetail())) // Перегрузка без ID

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

    override fun toMainMenuLessonMarks(lessonId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalLessonMarks(lessonId)
        )
    )

    override fun toMainMenuTeacherHomeworkList() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherHomeworkList()))

    override fun toMainMenuStudentHomeworkList() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentHomeworkList()))

    override fun toMainMenuStudentSchedule() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSchedule()))

    override fun toMainMenuLessonEdit() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalLessonEdit()))

    override fun toMainMenuSelectRole() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalSelectRole()))

    override fun toMainMenuStudentHome() = nav(NMD.actionGlobalHomeStudent())
    override fun toMainMenuStudentAdmin() = nav(NMD.actionGlobalHomeAdmin())
    override fun toMainMenuStudentTeacher() = nav(NMD.actionGlobalHomeTeacher())

    // --- RouterAdmin ---
    override fun toAdminSchoolsList() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToSchools()))

    override fun toAdminDashboard() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToDashboard()))

    override fun toAdminClassesList() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToClasses()))

    override fun toAdminSubjectsList() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToSubjects()))

    override fun toAdminRoomsList() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToRooms()))

    override fun toAdminAssignmentsList() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToAssignments()))

    override fun toAdminUsersList() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToUsers()))

    override fun toAdminAuditLog() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToAudit()))

    override fun toAdminSettings() =
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToSettings()))

    override fun toAdminEditAssignment(id: AssignmentId) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalTeacherAssignmentEdit(id)
        )
    )

    override fun toAdminEditClass(classId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalClassEdit(classId)
        )
    )

    override fun toAdminEditSubject(subjectId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalSubjectEdit(subjectId)
        )
    )

    override fun toAdminEditUser(userId: Int) = navigationManager.navigate(
        NavCommand.ToAction(
            NavMainMainDirections.actionGlobalUserEdit(userId)
        )
    )

    override fun toAdminEditSchool(schoolId: Int) {
        Timber.d("Edit school $schoolId – not implemented")
    }

    override fun toAdminProfile() =
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalProfile()))

    override fun toAuditLogDetail(id: Int) {
        navigationManager.navigate(
            NavCommand.ToAction(
                NavAdminDirections.actionToAuditDetail(id)
            )
        )
    }


    // --- RouterError ---
    override fun onUnauthorized(): () -> Unit =
        { navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding())) }

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

    // --- RouterBack ---
    override suspend fun toBack() = navigationManager.navigate(NavCommand.Back)

    private fun nav(navDirections: NavDirections) {
        navigationManager.navigate(NavCommand.ToAction(navDirections))
    }
}