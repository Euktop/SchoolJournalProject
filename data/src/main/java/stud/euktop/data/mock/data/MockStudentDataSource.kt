// data/src/main/java/stud/euktop/data/mock/data/MockStudentDataSource.kt
package stud.euktop.data.mock.data

import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.schedule.StudentScheduleItem
import stud.euktop.domain.model.student.StudentGradesSummary
import stud.euktop.domain.model.student.StudentMarksAggregated
import stud.euktop.domain.model.student.StudentOverallAverage
import stud.euktop.domain.utils.toDate
import java.util.Date

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
        StudentSubjectMark(1, "20.04.2024".toDate(), AbsenceTypes.G4, "Хорошо"),
        StudentSubjectMark(2, "21.04.2024".toDate(), AbsenceTypes.G5, "Молодец"),
        StudentSubjectMark(3, "22.04.2024".toDate(), AbsenceTypes.IRRESPECTABLE, "Без причины"),
        StudentSubjectMark(4, "23.04.2024".toDate(), AbsenceTypes.G3, null),
        StudentSubjectMark(5, "24.04.2024".toDate(), AbsenceTypes.G4, "Хорошо")
    )

    val studentGradesSummaryList = listOf(
        StudentGradesSummary(
            subjectId = 1, subjectName = "Алгебра", averageMark = 4.8, finalMark = 5.0,
            totalGrades = 12, goodGrades = 8, excellentGrades = 4
        ),
        StudentGradesSummary(
            subjectId = 2, subjectName = "Литература", averageMark = 4.5, finalMark = 4.0,
            totalGrades = 10, goodGrades = 6, excellentGrades = 4
        )
    )

    val studentMarksAggregatedList = listOf(
        StudentMarksAggregated(
            date = Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000),
            averageMark = 4.2,
            marksCount = 4
        ),
        StudentMarksAggregated(date = Date(), averageMark = 4.5, marksCount = 5)
    )

    val studentOverallAverage = StudentOverallAverage(
        averageMark = 4.4,
        totalGrades = 30,
        goodGrades = 18,
        excellentGrades = 10
    )

    fun getSubjectsSummary(studentId: Int): List<StudentSubjectSummary> = studentSubjectsSummary
    fun getSubjectMarks(studentId: Int, subjectId: Int): List<StudentSubjectMark> =
        studentSubjectMarks

    fun getGradesSummary(studentId: Int): List<StudentGradesSummary> = studentGradesSummaryList
    fun getMarksAggregated(studentId: Int, subjectId: Int): List<StudentMarksAggregated> =
        studentMarksAggregatedList

    fun getOverallAverage(studentId: Int): StudentOverallAverage = studentOverallAverage

    fun getStudentSchedule(studentId: Int): List<StudentScheduleItem> {
        val now = Date()
        return listOf(
            StudentScheduleItem(
                lessonId = 1,
                date = now,
                startTime = "09:00",
                endTime = "09:45",
                topic = "Квадратные уравнения",
                subjectName = "Математика",
                teacherLastName = "Петрова",
                teacherFirstName = "Анна",
                teacherSurName = "Сергеевна",
                roomName = "101",
                locationAddress = null
            ),
            StudentScheduleItem(
                lessonId = 2,
                date = now,
                startTime = "10:00",
                endTime = "10:45",
                topic = "Дискриминант",
                subjectName = "Математика",
                teacherLastName = "Петрова",
                teacherFirstName = "Анна",
                teacherSurName = "Сергеевна",
                roomName = "101",
                locationAddress = null
            )
        )
    }
}