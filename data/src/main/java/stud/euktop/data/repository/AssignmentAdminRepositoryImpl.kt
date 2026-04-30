package stud.euktop.data.repository

import stud.euktop.data.mock.MockAssignmentDataSource
import stud.euktop.data.mock.MockDelayService
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.repository.AssignmentAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentAdminRepositoryImpl @Inject constructor() : AssignmentAdminRepository {
    override suspend fun getTeacherAssignments(): Result<List<TeacherAssignment>> {
        MockDelayService.delay()
        return Result.success(MockAssignmentDataSource.getAll())
    }

    override suspend fun getTeacherAssignment(assignmentId: Int): Result<TeacherAssignment> {
        MockDelayService.delay()
        return MockAssignmentDataSource.get(assignmentId)?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("Assignment not found"))
    }

    override suspend fun addTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment> {
        MockDelayService.delay()
        return Result.success(MockAssignmentDataSource.add(assignment))
    }

    override suspend fun updateTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment> {
        MockDelayService.delay()
        MockAssignmentDataSource.update(assignment)
        return Result.success(assignment)
    }

    override suspend fun deleteTeacherAssignment(assignmentId: Int): Result<Unit> {
        MockDelayService.delay()
        return if (MockAssignmentDataSource.delete(assignmentId)) Result.success(Unit)
        else Result.failure(NoSuchElementException("Assignment not found"))
    }
}