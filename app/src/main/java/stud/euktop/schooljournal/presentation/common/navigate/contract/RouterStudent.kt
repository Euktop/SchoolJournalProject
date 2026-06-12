// RouterStudent.kt
package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterStudent {
    fun toStudentSubjectDetail(subjectId: Int)
    fun toStudentSchedule()
    fun toStudentSubjects()
    fun toStudentMarks()
    fun toStudentHomework()
    fun toStudentAnalytics()
    fun toStudentProfile()
}