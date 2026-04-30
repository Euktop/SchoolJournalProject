package stud.euktop.domain.repository

import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary

interface StudentRepository {
    suspend fun getSubjectsSummary(studentId: Int): Result<List<StudentSubjectSummary>>
    suspend fun getSubjectMarks(studentId: Int, subjectId: Int): Result<List<StudentSubjectMark>>
}