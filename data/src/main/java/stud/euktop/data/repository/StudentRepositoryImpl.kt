package stud.euktop.data.repository

import stud.euktop.data.MockData
import stud.euktop.domain.model.StudentSubjectMark
import stud.euktop.domain.model.StudentSubjectSummary
import stud.euktop.domain.repository.StudentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepositoryImpl @Inject constructor() : StudentRepository {
    override suspend fun getSubjectsSummary(studentId: Int): Result<List<StudentSubjectSummary>> =
        Result.success(MockData.studentSubjectsSummary)

    override suspend fun getSubjectMarks(
        studentId: Int,
        subjectId: Int
    ): Result<List<StudentSubjectMark>> =
        Result.success(MockData.studentSubjectMarks)
}