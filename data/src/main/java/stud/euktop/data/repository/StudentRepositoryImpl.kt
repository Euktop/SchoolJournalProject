package stud.euktop.data.repository

import stud.euktop.data.mock.MockDelayService
import stud.euktop.data.mock.MockStudentDataSource
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.repository.StudentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepositoryImpl @Inject constructor() : StudentRepository {
    override suspend fun getSubjectsSummary(studentId: Int): Result<List<StudentSubjectSummary>> {
        MockDelayService.delay()
        return Result.success(MockStudentDataSource.getSubjectsSummary(studentId))
    }

    override suspend fun getSubjectMarks(studentId: Int, subjectId: Int): Result<List<StudentSubjectMark>> {
        MockDelayService.delay()
        return Result.success(MockStudentDataSource.getSubjectMarks(studentId, subjectId))
    }
}