// data/src/main/java/stud/euktop/data/mock/repository/AssignmentAdminMockRepositoryImpl.kt
package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockAssignmentDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.util.filterParam
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.assignment.TeacherAssignmentFilter
import stud.euktop.domain.model.assignment.TeacherAssignmentUpdate
import stud.euktop.domain.repository.AssignmentAdminRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentAdminMockRepositoryImpl @Inject constructor() : AssignmentAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getTeacherAssignments(filter: TeacherAssignmentFilter): Result<List<TeacherAssignment>> {
        logger?.i(tag, "getTeacherAssignments started", "filter=$filter")
        MockDelayService.delay()
        return try {
            val all = MockAssignmentDataSource.getAll()
            val filtered = all.filter { assignment ->
                filterParam(
                    filter.teacherId to assignment.assignmentId.teacherId,
                    filter.classId to assignment.assignmentId.classId,
                    filter.subjectId to assignment.assignmentId.subjectId,
                    filter.isPrimary to assignment.isPrimary
                ) && (filter.validFrom == null || assignment.assignmentId.validFrom == filter.validFrom) &&
                        (filter.validTo == null || assignment.validToDate == filter.validTo)
            }
            val paged = filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
            logger?.i(tag, "getTeacherAssignments succeeded", "count=${paged.size}")
            Result.success(paged)
        } catch (e: Exception) {
            logger?.e(tag, "getTeacherAssignments failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getTeacherAssignment(id: AssignmentId): Result<TeacherAssignment> {
        logger?.i(tag, "getTeacherAssignment started", "id=$id")
        MockDelayService.delay()
        return try {
            val assignment = MockAssignmentDataSource.get(id)
            if (assignment != null) {
                logger?.i(tag, "getTeacherAssignment succeeded", "id=$id")
                Result.success(assignment)
            } else {
                val ex = NoSuchElementException("Assignment not found")
                logger?.e(tag, "getTeacherAssignment failed", ex, "id=$id not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "getTeacherAssignment exception", e, "id=$id")
            Result.failure(e)
        }
    }

    override suspend fun addTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment> {
        logger?.i(tag, "addTeacherAssignment started", "assignment=$assignment")
        MockDelayService.delay()
        return try {
            val newAssignment = MockAssignmentDataSource.add(assignment)
            logger?.i(tag, "addTeacherAssignment succeeded", "newId=${newAssignment.assignmentId}")
            Result.success(newAssignment)
        } catch (e: Exception) {
            logger?.e(tag, "addTeacherAssignment failed", e, "assignment=$assignment")
            Result.failure(e)
        }
    }

    override suspend fun updateTeacherAssignment(update: TeacherAssignmentUpdate): Result<TeacherAssignment> {
        logger?.i(tag, "updateTeacherAssignment started", "update=$update")
        MockDelayService.delay()
        return try {
            val existing = MockAssignmentDataSource.get(update.id)
                ?: return Result.failure(NoSuchElementException("Assignment not found"))

            val updated = existing.copy(
                validToDate = update.validToDate.uValue ?: existing.validToDate,
                isPrimary = update.isPrimary.uValue ?: existing.isPrimary
            )
            MockAssignmentDataSource.update(updated)
            logger?.i(tag, "updateTeacherAssignment succeeded", "id=${update.id}")
            Result.success(updated)
        } catch (e: Exception) {
            logger?.e(tag, "updateTeacherAssignment failed", e, "update=$update")
            Result.failure(e)
        }
    }

    override suspend fun deleteTeacherAssignment(id: AssignmentId): Result<Unit> {
        logger?.i(tag, "deleteTeacherAssignment started", "id=$id")
        MockDelayService.delay()
        return try {
            val deleted = MockAssignmentDataSource.delete(id)
            if (deleted) {
                logger?.i(tag, "deleteTeacherAssignment succeeded", "id=$id")
                Result.success(Unit)
            } else {
                val ex = NoSuchElementException("Assignment not found")
                logger?.e(tag, "deleteTeacherAssignment failed", ex, "id=$id not found")
                Result.failure(ex)
            }
        } catch (e: Exception) {
            logger?.e(tag, "deleteTeacherAssignment exception", e, "id=$id")
            Result.failure(e)
        }
    }
}