package stud.euktop.domain.repository

import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.school.ClassInfo

interface StudentRepository {
    suspend fun getSubjectsSummary(studentId: Int? = null): Result<List<StudentSubjectSummary>>
    suspend fun getSubjectMarks(subjectId: Int, studentId: Int? = null): Result<List<StudentSubjectMark>>
    suspend fun getStudentClass(studentId: Int? = null): Result<ClassInfo>
}