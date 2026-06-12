package stud.euktop.schooljournal.presentation.common.navigate.state

import stud.euktop.domain.model.user.Role
import stud.euktop.schooljournal.R

class DestinationRoleChecker {
    // Маппинг destination ID -> список разрешённых ролей
    private val allowedRolesMap = mapOf(
        // Экраны ученика
        R.id.studentHomeFragment to listOf(Role.STUDENT),
        R.id.studentSubjectsFragment to listOf(Role.STUDENT),
        R.id.studentSubjectDetailFragment to listOf(Role.STUDENT),
        R.id.studentScheduleFragment to listOf(Role.STUDENT, Role.TEACHER),
        R.id.studentHomeworkListFragment to listOf(Role.STUDENT),

        // Экраны учителя
        R.id.teacherHomeFragment to listOf(Role.TEACHER),
        R.id.teacherClassesFragment to listOf(Role.TEACHER),
        R.id.teacherLessonsFragment to listOf(Role.TEACHER),
        R.id.lessonMarksFragment to listOf(Role.TEACHER),
        R.id.teacherHomeworkListFragment to listOf(Role.TEACHER),
        R.id.teacherHomeworkEditFragment to listOf(Role.TEACHER, Role.ADMIN),

        // Экраны админа
        R.id.adminHomeFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.schoolsListFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.classesListFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.subjectsListFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.roomsListFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.assignmentsListFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.usersListFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.auditLogFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.settingsFragment to listOf(Role.ADMIN, Role.DIRECTOR, Role.TEACHER),
        R.id.userEditFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.classEditFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.subjectEditFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.teacherAssignmentEditFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.auditLogDetailFragment to listOf(Role.ADMIN, Role.DIRECTOR),
        R.id.roomEditFragment to listOf(Role.ADMIN, Role.DIRECTOR),

        // Общие
        R.id.mainProfileFragment to listOf(Role.STUDENT, Role.TEACHER, Role.ADMIN, Role.DIRECTOR),
        R.id.changePasswordFragment to listOf(Role.STUDENT, Role.TEACHER, Role.ADMIN, Role.DIRECTOR),
    )

    fun isDestinationAllowed(destinationId: Int, role: Role?): Boolean {
        if (role == null) return false
        return allowedRolesMap[destinationId]?.contains(role) == true
    }
}