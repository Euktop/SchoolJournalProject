// data/src/main/java/stud/euktop/data/mock/data/MockAssignmentDataSource.kt
package stud.euktop.data.mock.data

import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.assignment.TeacherAssignment
import java.text.SimpleDateFormat
import java.util.Locale

internal object MockAssignmentDataSource {
    private val _assignments = mutableListOf<TeacherAssignment>().apply {
        val teacher1 = MockUserDataSource.getUser(2)!!
        val teacher2 = MockUserDataSource.getUser(1)!!
        val class1 = MockClassDataSource.get(1)!!
        val class2 = MockClassDataSource.get(2)!!
        val subject1 = MockSubjectDataSource.get(1)!!
        val subject2 = MockSubjectDataSource.get(2)!!
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        add(
            TeacherAssignment(
                assignmentId = AssignmentId(
                    teacherId = teacher2.userId,
                    classId = class1.classId,
                    subjectId = subject1.subjectId,
                    validFrom = dateFormat.parse("01.09.2025")!!
                ),
                validToDate = null,
                isPrimary = true
            )
        )
        add(
            TeacherAssignment(
                assignmentId = AssignmentId(
                    teacherId = teacher1.userId,
                    classId = class2.classId,
                    subjectId = subject2.subjectId,
                    validFrom = dateFormat.parse("01.09.2025")!!
                ),
                validToDate = dateFormat.parse("30.05.2026")!!,
                isPrimary = false
            )
        )
    }

    fun getAll(): List<TeacherAssignment> = _assignments.toList()

    fun get(id: AssignmentId): TeacherAssignment? =
        _assignments.find { it.assignmentId == id }

    fun add(assignment: TeacherAssignment): TeacherAssignment {
        val newAssignment = assignment.copy(assignmentId = assignment.assignmentId)
        _assignments.add(newAssignment)
        return newAssignment
    }

    fun update(assignment: TeacherAssignment) {
        val index = _assignments.indexOfFirst { it.assignmentId == assignment.assignmentId }
        if (index >= 0) _assignments[index] = assignment
    }

    fun delete(id: AssignmentId): Boolean = _assignments.removeIf { it.assignmentId == id }
}