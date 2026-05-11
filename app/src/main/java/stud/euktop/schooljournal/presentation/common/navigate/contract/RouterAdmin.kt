package stud.euktop.schooljournal.presentation.common.navigate.contract

import stud.euktop.domain.model.assignment.AssignmentId

interface RouterAdmin {
    fun navigateBack()
    fun toEditUser(userId: Int)
    fun toEditClass(classId: Int)
    fun toEditSubject(subjectId: Int)
    fun toEditAssignment(assignmentId: AssignmentId)
}