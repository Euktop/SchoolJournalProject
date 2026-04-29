package stud.euktop.domain.repository

import stud.euktop.domain.model.TeacherClassItem

interface TeacherRepository {
    suspend fun getTeacherClasses(): Result<List<TeacherClassItem>>
}