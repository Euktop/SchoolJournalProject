package stud.euktop.domain.repository

import stud.euktop.domain.model.attendance.StudentMarkItem

interface LessonMarksRepository {
    suspend fun getMarks(lessonId: Int): Result<List<StudentMarkItem>>
}