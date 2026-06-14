@file:Suppress("DEPRECATION")

package stud.euktop.schooljournal.presentation.common.navigate.impl

import androidx.navigation.NavDirections
import kotlinx.coroutines.runBlocking
import stud.euktop.data.BuildConfig
import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.Nav1Directions
import stud.euktop.schooljournal.NavAdminDirections
import stud.euktop.schooljournal.NavAuthDirections
import stud.euktop.schooljournal.NavMainMainDirections
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.R
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
import stud.euktop.schooljournal.presentation.common.navigate.state.DestinationRoleChecker
import stud.euktop.schooljournal.presentation.common.navigate.state.NavigationStateManager
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouterImpl @Inject constructor(
    private val navigationManager: NavigationManager,
    private val authRepository: AuthRepository,
    private val roleRepository: RoleRepository,
    private val stateManager: NavigationStateManager,
    private val roleChecker: DestinationRoleChecker
) : RouterSplash, RouterMain, RouterAuth, RouterAdmin, RouterError, RouterMainMenu,
    RouterProfile, RouterStudent, RouterTeacher {

    // --- Вспомогательные методы ---
    private suspend fun checkRole(vararg allowedRoles: Role, action: String) {
        val currentRole = roleRepository.getCurrentRole()
        if (currentRole != null && currentRole !in allowedRoles) {
            Timber.w("Role check failed for action '$action': required $allowedRoles, current=$currentRole")
        }
    }

    private suspend fun navigateWithRoleCheck(
        action: () -> Unit,
        vararg allowedRoles: Role,
        actionName: String
    ) {
        checkRole(*allowedRoles, action = actionName)
        action()
    }

    // --- RouterSplash & RouterMain ---
    override suspend fun navigateAfterSplash() {
        if (authRepository.getCurrentUser().isSuccess) {
            val role = roleRepository.getCurrentRole() ?: return toLogin()
            val roleName = role.name
            val lastDest = stateManager.getLastDestination(roleName)

            if (lastDest != null && roleChecker.isDestinationAllowed(
                    lastDest.destinationId,
                    role
                )
            ) {
                // 1. Сначала переходим в основной граф (чистим стек от splash)
                // 2. Затем переходим к сохраненному экрану внутри этого графа
                navigationManager.navigate(
                    NavCommand.ToAction(Nav1Directions.actionGlobalNavMainMain()),
                    NavCommand.ToDestination(lastDest.destinationId)
                )
            } else {
                toMain()
            }
        } else {
            toLogin()
        }
    }

    override suspend fun toMain() {
        val mainGraphAction = NavCommand.ToAction(Nav1Directions.actionGlobalNavMainMain())

        if (BuildConfig.DEBUG) {
            // В дебаге просто открываем тестовое меню (startDestination графа nav_main_main)
            navigationManager.navigate(mainGraphAction)
        } else {
            val role = roleRepository.getCurrentRole()
            when (role) {
                Role.STUDENT -> navigationManager.navigate(
                    mainGraphAction,
                    NavCommand.ToAction(NavMainMainDirections.actionGlobalHomeStudent())
                )
                Role.TEACHER -> navigationManager.navigate(
                    mainGraphAction,
                    NavCommand.ToAction(NavMainMainDirections.actionGlobalHomeTeacher())
                )
                Role.ADMIN, Role.DIRECTOR -> navigationManager.navigate(
                    mainGraphAction,
                    NavCommand.ToAction(NavMainMainDirections.actionGlobalHomeAdmin())
                )
                else -> toLogin()
            }
        }
    }

    // --- RouterAuth ---
    override suspend fun toCreateProfile() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToAction(NavAuthDirections.actionGlobalToAuthProfile())
        )
    }

    override suspend fun toCreatePassword() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToAction(NavAuthDirections.actionGlobalToCreatePassword())
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
    override fun toProfile() {
        runBlocking {
            checkRole(
                Role.STUDENT,
                Role.TEACHER,
                Role.ADMIN,
                Role.DIRECTOR,
                action = "toProfile"
            )
        }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalProfile()))
    }

    override fun toEditUser(userId: Int) {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toEditUser") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalUserEdit(
                    userId
                )
            )
        )
    }

    override fun toChangePassword() {
        runBlocking {
            checkRole(
                Role.STUDENT,
                Role.TEACHER,
                Role.ADMIN,
                Role.DIRECTOR,
                action = "toChangePassword"
            )
        }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalChangePassword()))
    }

    override fun toLogout() {
        // Выполняем переход в onboarding и очищаем стек главного графа (nav1), чтобы гарантировать
        // отсутствие вернувшихся фрагментов после логаута (например, ProfileFragment).
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.PopUpTo(R.id.nav1, true)
        )
        runBlocking {
            val role = roleRepository.getCurrentRole()?.name ?: return@runBlocking
            stateManager.clearPending(role)
        }
    }

    // --- RouterStudent ---
    override fun toStudentSubjectDetail(subjectId: Int) {
        runBlocking { checkRole(Role.STUDENT, action = "toStudentSubjectDetail") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalStudentSubjectDetail(
                    subjectId
                )
            )
        )
    }

    override fun toStudentSchedule() {
        runBlocking { checkRole(Role.STUDENT, Role.TEACHER, action = "toStudentSchedule") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSchedule()))
    }

    override fun toStudentSubjects() {
        runBlocking { checkRole(Role.STUDENT, action = "toStudentSubjects") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSubjects()))
    }

    override fun toStudentMarks() {
        Timber.w("StudentMarks not implemented")
        // TODO: добавить экран оценок ученика
    }

    override fun toStudentHomework() {
        runBlocking { checkRole(Role.STUDENT, action = "toStudentHomework") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentHomeworkList()))
    }

    override fun toStudentAnalytics() {
        Timber.w("StudentAnalytics not implemented")
        // TODO: добавить аналитику ученика
    }

    override fun toStudentProfile() = toProfile()

    // --- RouterTeacher ---
    override fun toTeacherHomeworkEdit() {
        runBlocking { checkRole(Role.TEACHER, Role.ADMIN, action = "toTeacherHomeworkEdit") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherHomeworkEdit()))
    }

    override fun toTeacherHomeworkEdit(homeworkId: Int) {
        runBlocking { checkRole(Role.TEACHER, Role.ADMIN, action = "toTeacherHomeworkEdit") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalTeacherHomeworkEdit(
                    homeworkId
                )
            )
        )
    }

    override fun toTeacherLessons(classId: Int, subjectId: Int) {
        runBlocking { checkRole(Role.TEACHER, action = "toTeacherLessons") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalTeacherLessons(
                    classId,
                    subjectId
                )
            )
        )
    }

    override fun toTeacherLessonMarks(lessonId: Int) {
        runBlocking { checkRole(Role.TEACHER, action = "toTeacherLessonMarks") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalLessonMarks(
                    lessonId
                )
            )
        )
    }

    override fun toTeacherClasses() {
        runBlocking { checkRole(Role.TEACHER, action = "toTeacherClasses") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherClasses()))
    }

    override fun toTeacherHomeworkList() {
        runBlocking { checkRole(Role.TEACHER, action = "toTeacherHomeworkList") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalTeacherHomeworkList()))
    }

    override fun toTeacherSchedule() = toStudentSchedule() // общее расписание

    override fun toTeacherAnalytics() {
        Timber.w("TeacherAnalytics not implemented")
        // TODO: добавить аналитику учителя
    }

    override fun toTeacherSettings() {
        runBlocking { checkRole(Role.TEACHER, action = "toTeacherSettings") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalSettings()))
    }

    // --- RouterMainMenu ---
    override fun toMainMenuTeacherClasses() = toTeacherClasses()
    override fun toMainMenuStudentSubjects() = toStudentSubjects()
    override fun toMainMenuAdminPanel() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toMainMenuAdminPanel") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalHomeAdmin()))
    }

    override fun toMainMenuAuthProfile() {
        navigationManager.navigate(NavCommand.ToAction(NavAuthDirections.actionGlobalToAuthProfile()))
    }

    override fun toStudentSubjectDetail() {
        runBlocking { checkRole(Role.STUDENT, action = "toStudentSubjectDetail") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalStudentSubjectDetail()))
    }

    override fun toNavAuth() {
        navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()))
    }

    override fun toNavAuthWithProfile() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToAction(NavAuthDirections.actionGlobalToAuthProfile())
        )
    }

    override fun toNavAuthWithCreatePassword() {
        navigationManager.navigate(
            NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()),
            NavCommand.ToAction(NavAuthDirections.actionGlobalToCreatePassword())
        )
    }

    override fun toMainMenuLessonMarks(lessonId: Int) = toTeacherLessonMarks(lessonId)
    override fun toMainMenuTeacherHomeworkList() = toTeacherHomeworkList()
    override fun toMainMenuStudentHomeworkList() = toStudentHomework()
    override fun toMainMenuStudentSchedule() = toStudentSchedule()
    override fun toMainMenuLessonEdit() {
        runBlocking {
            checkRole(
                Role.ADMIN,
                Role.DIRECTOR,
                Role.TEACHER,
                action = "toMainMenuLessonEdit"
            )
        }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalLessonEdit()))
    }

    override fun toMainMenuSelectRole() {
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalSelectRole()))
    }

    override fun toMainMenuStudentHome() {
        runBlocking { checkRole(Role.STUDENT, action = "toMainMenuStudentHome") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalHomeStudent()))
    }

    override fun toMainMenuStudentAdmin() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toMainMenuStudentAdmin") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalHomeAdmin()))
    }

    override fun toMainMenuStudentTeacher() {
        runBlocking { checkRole(Role.TEACHER, action = "toMainMenuStudentTeacher") }
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalHomeTeacher()))
    }

    // --- RouterAdmin ---
    override fun toAdminSchoolsList() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminSchoolsList") }
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToSchools()))
    }

    override fun toAdminDashboard() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminDashboard") }
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToDashboard()))
    }

    override fun toAdminClassesList() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminClassesList") }
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToClasses()))
    }

    override fun toAdminSubjectsList() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminSubjectsList") }
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToSubjects()))
    }

    override fun toAdminRoomsList() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminRoomsList") }
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToRooms()))
    }

    override fun toAdminAssignmentsList() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminAssignmentsList") }
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToAssignments()))
    }

    override fun toAdminUsersList() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminUsersList") }
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToUsers()))
    }

    override fun toAdminAuditLog() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminAuditLog") }
        navigationManager.navigate(NavCommand.ToAction(NavAdminDirections.actionToAudit()))
    }

    override fun toAdminSettings() {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminSettings") }
        // Открываем Settings через главный граф, чтобы экран был сквозным для всех ролей
        navigationManager.navigate(NavCommand.ToAction(NavMainMainDirections.actionGlobalSettings()))
    }

    override fun toAdminEditAssignment(id: AssignmentId) {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminEditAssignment") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalTeacherAssignmentEdit(
                    id
                )
            )
        )
    }

    override fun toAdminEditClass(classId: Int) {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminEditClass") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalClassEdit(
                    classId
                )
            )
        )
    }

    override fun toAdminEditSubject(subjectId: Int) {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminEditSubject") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalSubjectEdit(
                    subjectId
                )
            )
        )
    }

    override fun toAdminEditUser(userId: Int) = toEditUser(userId)

    override fun toAdminEditSchool(schoolId: Int) {
        Timber.w("School editing not implemented")
        // TODO: добавить SchoolEditFragment
    }

    override fun toAdminProfile() = toProfile()

    override fun toAuditLogDetail(id: Int) {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAuditLogDetail") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalAuditDetail(
                    id
                )
            )
        )
    }

    override fun toAdminEditRoom(roomId: Int) {
        runBlocking { checkRole(Role.ADMIN, Role.DIRECTOR, action = "toAdminEditRoom") }
        navigationManager.navigate(
            NavCommand.ToAction(
                NavMainMainDirections.actionGlobalRoomEdit(
                    roomId
                )
            )
        )
    }

    // --- RouterError ---
    override fun onUnauthorized(): () -> Unit = {
        runBlocking {
            // Сохраняем текущий маршрут для восстановления после логина
            // (сохранение происходит в NavigationManagerImpl автоматически)
            navigationManager.navigate(NavCommand.ToAction(Nav1Directions.actionGlobalToOnboarding()))
        }
    }

    override fun onAccessDenied(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
    override fun onNetworkConnection(): () -> Unit = {}
    override fun onServerError(): () -> Unit = {}
    override fun onNotFound(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
    override fun onConflict(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
    override fun onSessionExpired(): () -> Unit = onUnauthorized()
    override fun onInvalidCredentials(): () -> Unit = {}
    override fun onUserBlocked(): () -> Unit = {}
    override fun onUserDeleted(): () -> Unit = onUnauthorized()
    override fun onFieldRequired(): () -> Unit = {}
    override fun onRecordNotFound(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }
    override fun onUniqueViolation(): () -> Unit = {}
    override fun onForeignKeyViolation(): () -> Unit =
        { navigationManager.navigate(NavCommand.Back) }

    override fun onRecordAlreadyFixed(): () -> Unit = {}
    override fun onTeacherCannotTeachSubject(): () -> Unit = {}
    override fun onStudentNotInClass(): () -> Unit = {}
    override fun onNoPermissionToGrade(): () -> Unit = {}
    override fun onNoPermissionToManageUser(): () -> Unit =
        { navigationManager.navigate(NavCommand.Back) }

    override fun onCannotDeleteClassWithStudents(): () -> Unit = {}
    override fun onCannotDeleteClassWithLessons(): () -> Unit = {}
    override fun onRoomNotBelongsToSchool(): () -> Unit = {}
    override fun onTeacherTimeConflict(): () -> Unit = {}
    override fun onClassTimeConflict(): () -> Unit = {}
    override fun onStartTimeAfterEndTime(): () -> Unit = {}
    override fun onCannotDeleteSchoolWithClasses(): () -> Unit = {}
    override fun onCannotDeleteRoomUsedInLessons(): () -> Unit = {}
    override fun onClassTimeOverlap(): () -> Unit = {}
    override fun onMultiplePrimaryTeachers(): () -> Unit = {}
    override fun onPermissionOutOfRange(): () -> Unit = {}
    override fun onDefault(): () -> Unit = { navigationManager.navigate(NavCommand.Back) }

    // --- RouterBack ---
    override suspend fun toBack() = navigationManager.navigate(NavCommand.Back)

    private fun nav(navDirections: NavDirections) {
        navigationManager.navigate(NavCommand.ToAction(navDirections))
    }
}