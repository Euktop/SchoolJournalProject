package stud.euktop.data.repository

import com.schooljournal.api.TeacherAssignmentsApi
import com.schooljournal.model.CreateTeacherAssignmentRequest
import stud.euktop.data.map.fromDomain
import stud.euktop.data.map.toDate
import stud.euktop.data.map.toLocalDateTime
import stud.euktop.data.map.toTeacherAssignment
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.assignment.TeacherAssignmentFilter
import stud.euktop.domain.model.assignment.TeacherAssignmentUpdate
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.repository.AssignmentAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentAdminRepositoryImpl @Inject constructor(
    private val assignmentsApi: TeacherAssignmentsApi,
    private val errorHandler: ApiErrorHandler
) : AssignmentAdminRepository {

    override suspend fun getTeacherAssignments(filter: TeacherAssignmentFilter): Result<List<TeacherAssignment>> =
        errorHandler.safeApiCall {
            val dtos = assignmentsApi.apiTeacherAssignmentsFilterGet(
                teacherId = filter.teacherId,
                classId = filter.classId,
                subjectId = filter.subjectId,
                isPrimary = filter.isPrimary,
                validFrom = filter.validFrom?.toLocalDateTime(),
                validTo = filter.validTo?.toLocalDateTime(),
                filterByValidTo = filter.filterByValidTo,
                offset = filter.pagination.offset,
                limit = filter.pagination.limit
            )
            dtos.map { it.toTeacherAssignment() }
        }

    override suspend fun getTeacherAssignment(id: AssignmentId): Result<TeacherAssignment> =
        errorHandler.safeApiCall {
            val dtos = assignmentsApi.apiTeacherAssignmentsFilterGet(
                teacherId = id.teacherId,
                classId = id.classId,
                subjectId = id.subjectId,
                validFrom = id.validFrom.toLocalDateTime()
            )
            val assignment = dtos.firstOrNull()
                ?: throw DataError.RecordNotFound("Teacher assignment not found")
            assignment.toTeacherAssignment()
        }

    override suspend fun addTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment> =
        errorHandler.safeApiCall {
            val request = CreateTeacherAssignmentRequest().fromDomain(assignment)
            val result = assignmentsApi.apiTeacherAssignmentsPost(request)
            getTeacherAssignment(
                AssignmentId(
                    teacherId = result.teacherId ?: assignment.assignmentId.teacherId,
                    classId = result.classId ?: assignment.assignmentId.classId,
                    subjectId = result.subjectId ?: assignment.assignmentId.subjectId,
                    validFrom = result.validFrom?.toDate() ?: assignment.assignmentId.validFrom
                )
            ).getOrThrow()
        }

    override suspend fun updateTeacherAssignment(update: TeacherAssignmentUpdate): Result<TeacherAssignment> =
        errorHandler.safeApiCall {
            assignmentsApi.apiTeacherAssignmentsPatch(
                teacherId = update.id.teacherId,
                classId = update.id.classId,
                subjectId = update.id.subjectId,
                validFrom = update.id.validFrom.toLocalDateTime(),
                validTo = update.validToDate.uValue?.toLocalDateTime(),
                isPrimary = update.isPrimary.uValue,
                comment = update.comment.uValue,
                commentUpdate = update.comment.isUpdate,
                validToUpdate = update.validToDate.isUpdate,
            )
            getTeacherAssignment(update.id).getOrThrow()
        }

    override suspend fun deleteTeacherAssignment(id: AssignmentId): Result<Unit> =
        errorHandler.safeApiCall {
            assignmentsApi.apiTeacherAssignmentsDelete(
                teacherId = id.teacherId,
                classId = id.classId,
                subjectId = id.subjectId,
                validFrom = id.validFrom.toLocalDateTime()
            )
        }
}