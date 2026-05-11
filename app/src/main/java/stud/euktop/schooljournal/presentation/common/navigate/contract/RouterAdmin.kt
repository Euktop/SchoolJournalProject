package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterAdmin {
    fun navigateBack()
    fun toEditUser(userId: Int)
    fun toEditClass(classId: Int)
    fun toEditSubject(subjectId: Int)
    fun toEditAssignment(assignmentId: Int)
}