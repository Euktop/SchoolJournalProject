package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class StudentSubjectDetailState(
    val subjectName: String = "",
    val teacherName: String = "",
    val averageMark: String = "",
    val trend: String = "",
    val nextLesson: String = "",
    val grades: List<StudentSubjectGradeItem> = emptyList(),
    val selectedTab: Int = 0,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<StudentSubjectDetailState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): StudentSubjectDetailState =
        copy(loadingMap = loadingMap)
}