// RouterStudent.kt
package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterStudent {
    fun toStudentSubjectDetail(subjectId: Int)
    fun toSchedule()
    fun toSubjects()
    fun toMarks()
    fun toHomework()
    fun toAnalytics()
    fun toSettings()
    fun toProfile()
}