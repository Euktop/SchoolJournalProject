package stud.euktop.data.mock

import stud.euktop.domain.model.assignment.TeacherAssignment
import java.text.SimpleDateFormat
import java.util.Locale

object MockAssignmentDataSource {
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
                id = 1,
                teacher = teacher1,
                classInfo = class1,
                subject = subject1,
                validFromDate = dateFormat.parse("01.09.2025")!!,
                validToDate = null,
                isPrimary = true
            )
        )
        add(
            TeacherAssignment(
                id = 2,
                teacher = teacher2,
                classInfo = class2,
                subject = subject2,
                validFromDate = dateFormat.parse("01.09.2025")!!,
                validToDate = dateFormat.parse("30.05.2026")!!,
                isPrimary = false
            )
        )
    }

    fun getAll(): List<TeacherAssignment> = _assignments.toList()
    fun get(id: Int): TeacherAssignment? = _assignments.find { it.id == id }
    fun add(assignment: TeacherAssignment): TeacherAssignment {
        val newId = (_assignments.maxOfOrNull { it.id } ?: 0) + 1
        val newAssignment = assignment.copy(id = newId)
        _assignments.add(newAssignment)
        return newAssignment
    }
    fun update(assignment: TeacherAssignment) {
        val index = _assignments.indexOfFirst { it.id == assignment.id }
        if (index >= 0) _assignments[index] = assignment
    }
    fun delete(id: Int): Boolean = _assignments.removeIf { it.id == id }
}