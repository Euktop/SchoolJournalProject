// RouterTeacher.kt
package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterTeacher : RouterBack {
    fun toTeacherHomeworkEdit()
    fun toTeacherHomeworkEdit(homeworkId: Int)
    fun toTeacherLessons(classId: Int, subjectId: Int)
    fun toLessonMarks(lessonId: Int)
}