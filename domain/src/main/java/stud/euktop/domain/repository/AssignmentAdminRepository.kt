package stud.euktop.domain.repository

import stud.euktop.domain.model.assignment.TeacherAssignment

interface AssignmentAdminRepository {
    suspend fun getTeacherAssignments(): Result<List<TeacherAssignment>>
    suspend fun getTeacherAssignment(assignmentId: Int): Result<TeacherAssignment>
    suspend fun addTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment>
    suspend fun updateTeacherAssignment(assignment: TeacherAssignment): Result<TeacherAssignment>
    suspend fun deleteTeacherAssignment(assignmentId: Int): Result<Unit>
}