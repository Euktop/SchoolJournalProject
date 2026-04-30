package stud.euktop.domain.repository

import stud.euktop.domain.model.school.Subject

interface SubjectAdminRepository {
    suspend fun getSubjects(): Result<List<Subject>>
    suspend fun getSubject(subjectId: Int): Result<Subject>
    suspend fun addSubject(subject: Subject): Result<Subject>
    suspend fun updateSubject(subject: Subject): Result<Subject>
    suspend fun deleteSubject(subjectId: Int): Result<Unit>
}