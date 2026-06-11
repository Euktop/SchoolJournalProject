package stud.euktop.schooljournal.presentation.common.navigate.contract

import stud.euktop.domain.model.assignment.AssignmentId

interface RouterAdmin : RouterBack {
    fun toSchoolsList()
    fun toDashboard()
    fun toClassesList()
    fun toSubjectsList()
    fun toRoomsList()
    fun toAssignmentsList()
    fun toUsersList()
    fun toAuditLog()
    fun toSettings()
    fun toEditAssignment(id: AssignmentId)
    fun toEditClass(classId: Int)
    fun toEditSubject(subjectId: Int)
    fun toEditUser(userId: Int)
    fun toEditSchool(schoolId: Int)
    fun toProfile()
}