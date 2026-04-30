package stud.euktop.domain.repository

import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.TeacherClassItem

interface TeacherRepository {
    suspend fun getTeacherClasses(): Result<List<TeacherClassItem>>
    suspend fun getTeacherLessons(): Result<List<Lesson>>
}