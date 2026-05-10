package stud.euktop.domain.repository

import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.school.SubjectUpdate

interface SubjectAdminRepository {
    suspend fun getSubjects(filter: SubjectFilter = SubjectFilter()): Result<List<Subject>>
    suspend fun getSubject(subjectId: Int): Result<Subject>
    suspend fun addSubject(subject: Subject): Result<Subject>
    suspend fun updateSubject(subject: SubjectUpdate): Result<Subject>
    suspend fun deleteSubject(subjectId: Int): Result<Unit>
}