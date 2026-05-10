package stud.euktop.domain.repository

import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.assignment.TeacherAssignmentFilter
import stud.euktop.domain.model.assignment.TeacherAssignmentUpdate
interface AssignmentAdminRepository {
    suspend fun getTeacherAssignments(filter: TeacherAssignmentFilter = TeacherAssignmentFilter()): Result<List<TeacherAssignment>>
    suspend fun getTeacherAssignment(id: AssignmentId): Result<TeacherAssignment>
    suspend fun addTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment>
    suspend fun updateTeacherAssignment(update: TeacherAssignmentUpdate): Result<TeacherAssignment>
    suspend fun deleteTeacherAssignment(id: AssignmentId): Result<Unit>
}