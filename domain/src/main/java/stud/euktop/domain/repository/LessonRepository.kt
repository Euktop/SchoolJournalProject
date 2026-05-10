package stud.euktop.domain.repository

import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.lesson.LessonFilter
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.model.lesson.LessonUpdate

interface LessonRepository {
    suspend fun getLesson(lessonId: Int): Result<LessonFull>
    suspend fun getLessons(filter: LessonFilter = LessonFilter()): Result<List<LessonFull>>
    suspend fun updateLesson(update: LessonUpdate): Result<Lesson>
    suspend fun addLesson(lesson: Lesson): Result<LessonFull>
    suspend fun deleteLesson(lessonId: Int): Result<Unit>
}