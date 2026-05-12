package stud.euktop.data.mock.repository

import stud.euktop.data.mock.data.MockAssignmentDataSource
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.mock.util.filterParam
import stud.euktop.data.utils.ApiErrorHandler
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
class AssignmentAdminMockRepositoryImpl @Inject constructor(
    private val apiErrorHandler: ApiErrorHandler
) : AssignmentAdminRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getTeacherAssignments(filter: TeacherAssignmentFilter): Result<List<TeacherAssignment>> {
        logger?.i(tag, "getTeacherAssignments started", "filter=$filter")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
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
            filtered.drop(filter.pagination.offset ?: 0)
                .take(filter.pagination.limit ?: Int.MAX_VALUE)
        }
    }

    override suspend fun getTeacherAssignment(id: AssignmentId): Result<TeacherAssignment> {
        logger?.i(tag, "getTeacherAssignment started", "id=$id")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockAssignmentDataSource.get(id) ?: throw NoSuchElementException("Assignment not found")
        }
    }

    override suspend fun addTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment> {
        logger?.i(tag, "addTeacherAssignment started", "assignment=$assignment")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            MockAssignmentDataSource.add(assignment)
        }
    }

    override suspend fun updateTeacherAssignment(update: TeacherAssignmentUpdate): Result<TeacherAssignment> {
        logger?.i(tag, "updateTeacherAssignment started", "update=$update")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            val existing = MockAssignmentDataSource.get(update.id)
                ?: throw NoSuchElementException("Assignment not found")
            val updated = existing.copy(
                validToDate = update.validToDate.uValue ?: existing.validToDate,
                isPrimary = update.isPrimary.uValue ?: existing.isPrimary
            )
            MockAssignmentDataSource.update(updated)
            updated
        }
    }

    override suspend fun deleteTeacherAssignment(id: AssignmentId): Result<Unit> {
        logger?.i(tag, "deleteTeacherAssignment started", "id=$id")
        return apiErrorHandler.safeApiCall {
            MockDelayService.delay()
            if (!MockAssignmentDataSource.delete(id))
                throw NoSuchElementException("Assignment not found")
        }
    }
}