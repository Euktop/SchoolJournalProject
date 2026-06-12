package stud.euktop.schooljournal.presentation.common.navigate.contract

import stud.euktop.domain.model.assignment.AssignmentId

interface RouterAdmin : RouterBack {
    fun toAdminSchoolsList()
    fun toAdminDashboard()
    fun toAdminClassesList()
    fun toAdminSubjectsList()
    fun toAdminRoomsList()
    fun toAdminAssignmentsList()
    fun toAdminUsersList()
    fun toAdminAuditLog()
    fun toAdminSettings()
    fun toAdminEditAssignment(id: AssignmentId)
    fun toAdminEditClass(classId: Int)
    fun toAdminEditSubject(subjectId: Int)
    fun toAdminEditUser(userId: Int)
    fun toAdminEditSchool(schoolId: Int)
    fun toAdminProfile()
}