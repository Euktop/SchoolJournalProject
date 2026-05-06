// AssignmentAdminRepositoryImpl.kt
package stud.euktop.data.repository

import stud.euktop.data.mock.MockAssignmentDataSource
import stud.euktop.data.mock.MockDelayService
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.repository.AssignmentAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentAdminRepositoryImpl @Inject constructor() : AssignmentAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getTeacherAssignments(): Result<List<TeacherAssignment>> {
        logger?.i(tag, "getTeacherAssignments started")
        MockDelayService.delay()
        return try {
            val result = MockAssignmentDataSource.getAll()
            logger?.i(tag, "getTeacherAssignments succeeded", "count=${result.size}")
            Result.success(result)
        } catch (e: Exception) {
            logger?.e(tag, "getTeacherAssignments failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getTeacherAssignment(assignmentId: Int): Result<TeacherAssignment> {
        logger?.i(tag, "getTeacherAssignment started", "assignmentId=$assignmentId")
        MockDelayService.delay()
        return try {
            val assignment = MockAssignmentDataSource.get(assignmentId)
            if (assignment != null) {
                logger?.i(tag, "getTeacherAssignment succeeded", "assignmentId=$assignmentId")
                Result.success(assignment)
            } else {
                val ex = NoSuchElementException("Assignment not found")
                logger?.e(
                    tag,
                    "getTeacherAssignment failed",
                    ex,
                    "assignmentId=$assignmentId not found"
                )
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getTeacherAssignment exception", e, "assignmentId=$assignmentId")
            Result.failure(e)
        }
    }

    override suspend fun addTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment> {
        logger?.i(tag, "addTeacherAssignment started", "assignment=$assignment")
        MockDelayService.delay()
        return try {
            val newAssignment = MockAssignmentDataSource.add(assignment)
            logger?.i(tag, "addTeacherAssignment succeeded", "newId=${newAssignment.id}")
            Result.success(newAssignment)
        } catch (e: Exception) {
            logger?.e(tag, "addTeacherAssignment failed", e, "assignment=$assignment")
            Result.failure(e)
        }
    }

    override suspend fun updateTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment> {
        logger?.i(tag, "updateTeacherAssignment started", "assignment=$assignment")
        MockDelayService.delay()
        return try {
            MockAssignmentDataSource.update(assignment)
            logger?.i(tag, "updateTeacherAssignment succeeded", "assignmentId=${assignment.id}")
            Result.success(assignment)
        } catch (e: Exception) {
            logger?.e(tag, "updateTeacherAssignment failed", e, "assignment=$assignment")
            Result.failure(e)
        }
    }

    override suspend fun deleteTeacherAssignment(assignmentId: Int): Result<Unit> {
        logger?.i(tag, "deleteTeacherAssignment started", "assignmentId=$assignmentId")
        MockDelayService.delay()
        return try {
            val deleted = MockAssignmentDataSource.delete(assignmentId)
            if (deleted) {
                logger?.i(tag, "deleteTeacherAssignment succeeded", "assignmentId=$assignmentId")
                Result.success(Unit)
            } else {
                val ex = NoSuchElementException("Assignment not found")
                logger?.e(
                    tag,
                    "deleteTeacherAssignment failed",
                    ex,
                    "assignmentId=$assignmentId not found"
                )
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "deleteTeacherAssignment exception", e, "assignmentId=$assignmentId")
            Result.failure(e)
        }
    }
}