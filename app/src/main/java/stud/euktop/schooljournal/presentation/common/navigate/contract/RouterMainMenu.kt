// RouterMainMenu.kt
package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterMainMenu {
    fun toMainMenuTeacherClasses()
    fun toMainMenuStudentSubjects()
    fun toMainMenuAdminPanel()
    fun toMainMenuAuthProfile()
    fun toStudentSubjectDetail()
    fun toNavAuth()
    fun toNavAuthWithProfile()
    fun toNavAuthWithCreatePassword()
    fun toMainMenuLessonMarks(lessonId: Int)
    fun toMainMenuTeacherHomeworkList()
    fun toMainMenuStudentHomeworkList()
    fun toMainMenuStudentSchedule()
    fun toMainMenuLessonEdit()
    fun toMainMenuSelectRole()
    fun toMainMenuStudentHome()
    fun toMainMenuStudentAdmin()
    fun toMainMenuStudentTeacher()
}