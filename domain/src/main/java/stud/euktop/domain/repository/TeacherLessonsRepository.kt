package stud.euktop.domain.repository

import stud.euktop.domain.model.TeacherLessonItem

interface TeacherLessonsRepository {
    suspend fun getLessons(classId: Int, subjectId: Int): Result<List<TeacherLessonItem>>
}