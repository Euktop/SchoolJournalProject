package stud.euktop.domain.repository

import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.model.lesson.LessonFilter2

interface LessonRepository {
    suspend fun getLesson(lessonId: Int): Result<Lesson>
    suspend fun getLessons(filter: LessonFilter): Result<List<Lesson>>
    suspend fun getLessons(filter: LessonFilter2): Result<List<Lesson>>
    suspend fun addLesson(lesson: Lesson): Result<Lesson>
    suspend fun updateLesson(lesson: Lesson): Result<Lesson>
    suspend fun deleteLesson(lessonId: Int): Result<Unit>
}