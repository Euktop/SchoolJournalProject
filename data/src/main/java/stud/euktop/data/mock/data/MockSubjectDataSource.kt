package stud.euktop.data.mock.data

import stud.euktop.domain.model.school.Subject

internal object MockSubjectDataSource {
    private val _subjects = mutableListOf<Subject>().apply {
        add(Subject(1, "Математика", "Алгебра и геометрия"))
        add(Subject(2, "Русский язык", null))
        add(Subject(3, "Физика", null))
    }

    fun getAll(): List<Subject> = _subjects.toList()
    fun get(subjectId: Int): Subject? = _subjects.find { it.subjectId == subjectId }
    fun add(subject: Subject): Subject {
        val newId = (_subjects.maxOfOrNull { it.subjectId } ?: 0) + 1
        val newSubject = subject.copy(subjectId = newId)
        _subjects.add(newSubject)
        return newSubject
    }
    fun update(subject: Subject) {
        val index = _subjects.indexOfFirst { it.subjectId == subject.subjectId }
        if (index >= 0) _subjects[index] = subject
    }
    fun delete(subjectId: Int): Boolean = _subjects.removeIf { it.subjectId == subjectId }
}