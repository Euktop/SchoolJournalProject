package stud.euktop.data.mock

import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.model.attendance.StudentMarkItem

object MockLessonMarksDataSource {
    val marks = listOf(
        StudentMarkItem(4, "Борисова", "Вера", "Владимировна", AbsenceTypes.G5, "Молодец"),
        StudentMarkItem(5, "Дмитриева", "Елена", null, AbsenceTypes.G4, null),
        StudentMarkItem(3, "Сидоров", "Алексей", null, AbsenceTypes.ILL, "Болеет"),
        StudentMarkItem(2, "Петрова", "Анна", "Сергеевна", AbsenceTypes.G3, null)
    )

    fun getMarks(lessonId: Int): List<StudentMarkItem> = marks
}