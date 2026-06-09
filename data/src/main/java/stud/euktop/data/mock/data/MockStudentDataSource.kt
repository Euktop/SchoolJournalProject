// data/src/main/java/stud/euktop/data/mock/data/MockStudentDataSource.kt
package stud.euktop.data.mock.data

import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.utils.toDate

internal object MockStudentDataSource {
    val studentSubjectsSummary = listOf(
        StudentSubjectSummary(
            subjectId = 1,
            subjectName = "Алгебра",
            averageMark = 4.9,
            finalMark = 5,
            teacherName = "Смирнова О.П.",
            attendancePercent = 98
        ),
        StudentSubjectSummary(
            subjectId = 2,
            subjectName = "Литература",
            averageMark = 4.5,
            finalMark = 4,
            teacherName = "Иванова М.А.",
            attendancePercent = 78
        ),
        StudentSubjectSummary(
            subjectId = 3,
            subjectName = "Физика",
            averageMark = 3.8,
            finalMark = 4,
            teacherName = "Петров С.В.",
            attendancePercent = 88
        )
    )

    val studentSubjectMarks = listOf(
        StudentSubjectMark(1, "20.04.2026".toDate(), AbsenceTypes.G4, "Хорошо"),
        StudentSubjectMark(2, "21.04.2026".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark(3, "22.04.2026".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины"),
        StudentSubjectMark(4, "23.04.2026".toDate(), AbsenceTypes.G3, null),
        StudentSubjectMark(5, "24.04.2026".toDate(), AbsenceTypes.G4, "Хорошо"),
        StudentSubjectMark(6, "25.04.2026".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark(7, "26.04.2026".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины"),
        StudentSubjectMark(8, "27.04.2026".toDate(), AbsenceTypes.ILL, null),
        StudentSubjectMark(9, "28.04.2026".toDate(), AbsenceTypes.G2, "Хорошо"),
        StudentSubjectMark(10, "29.04.2026".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark(11, "30.04.2026".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины"),
        StudentSubjectMark(12, "31.04.2026".toDate(), AbsenceTypes.G3, null),
        StudentSubjectMark(13, "01.05.2026".toDate(), AbsenceTypes.G2, "Хорошо"),
        StudentSubjectMark(14, "02.05.2026".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark(15, "03.05.2026".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины"),
        StudentSubjectMark(16, "04.05.2026".toDate(), AbsenceTypes.ILL, null)
    )

    fun getSubjectsSummary(studentId: Int): List<StudentSubjectSummary> = studentSubjectsSummary
    fun getSubjectMarks(studentId: Int, subjectId: Int): List<StudentSubjectMark> =
        studentSubjectMarks
}