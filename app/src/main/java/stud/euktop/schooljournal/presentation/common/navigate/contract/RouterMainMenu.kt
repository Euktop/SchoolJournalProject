// RouterMainMenu.kt
package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterMainMenu {
    fun toTeacherClasses()
    fun toStudentSubjects()
    fun toAdminPanel()
    fun toAuthProfile()
    fun toStudentSubjectDetail()
    fun toNavAuth()
    fun toNavAuthWithProfile()
    fun toNavAuthWithCreatePassword()
    fun toLessonMarks(lessonId: Int)
    fun toTeacherHomeworkList()
    fun toStudentHomeworkList()
    fun toStudentSchedule()
    fun toLessonEdit()
    fun toSelectRole()
    fun toStudentHome()
    fun toStudentAdmin()
    fun toStudentTeacher()
}