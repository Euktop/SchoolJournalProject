package stud.euktop.data.mock

import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.utils.toDate

object MockStudentDataSource {
    val studentSubjectsSummary = listOf(
        StudentSubjectSummary(1, "Математика", 4.5, 5, "Иванова А.А."),
        StudentSubjectSummary(2, "Русский язык", 3.8, 4, "Петрова С.С."),
        StudentSubjectSummary(3, "Физика", 4.2, 4, "Сидоров В.В.")
    )

    val studentSubjectMarks = listOf(
        StudentSubjectMark("20.04.2026".toDate(), AbsenceTypes.G4, "Хорошо"),
        StudentSubjectMark("21.04.2026".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark("22.04.2026".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины"),
        StudentSubjectMark("23.04.2026".toDate(), AbsenceTypes.G3, null),
        StudentSubjectMark("24.04.2026".toDate(), AbsenceTypes.G4, "Хорошо"),
        StudentSubjectMark("25.04.2026".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark("26.04.2026".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины"),
        StudentSubjectMark("27.04.2026".toDate(), AbsenceTypes.ILL, null),
        StudentSubjectMark("28.04.2026".toDate(), AbsenceTypes.G2, "Хорошо"),
        StudentSubjectMark("29.04.2026".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark("30.04.2026".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины Без причины Без причины Без причины"),
        StudentSubjectMark("31.04.2026".toDate(), AbsenceTypes.G3, null),
        StudentSubjectMark("01.05.2026".toDate(), AbsenceTypes.G2, "Хорошо"),
        StudentSubjectMark("02.05.2026".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark("03.05.2026".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины"),
        StudentSubjectMark("04.05.2026".toDate(), AbsenceTypes.ILL, null),
    )

    fun getSubjectsSummary(studentId: Int): List<StudentSubjectSummary> = studentSubjectsSummary
    fun getSubjectMarks(studentId: Int, subjectId: Int): List<StudentSubjectMark> =
        studentSubjectMarks
}