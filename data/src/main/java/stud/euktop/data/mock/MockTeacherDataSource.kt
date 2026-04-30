package stud.euktop.data.mock

import stud.euktop.domain.model.lesson.TeacherClassItem
import stud.euktop.domain.model.lesson.TeacherLessonItem
import stud.euktop.domain.utils.toDate

object MockTeacherDataSource {
    private val _teacherClasses = listOf(
        TeacherClassItem(1, "Школа №1", 5, "А", 2025, 2026, 1, "Математика"),
        TeacherClassItem(2, "Школа №1", 5, "А", 2025, 2026, 2, "Русский язык"),
        TeacherClassItem(3, "Школа №2", 6, "Б", 2025, 2026, 3, "Физика")
    )

    private val _lessons = listOf(
        TeacherLessonItem(
            lessonId = 101,
            date = "28.04.2026".toDate(),
            topic = "Квадратные уравнения",
            startTime = "09:00",
            endTime = "09:45",
            roomName = "42",
            teacherName = "Иванова А.А."
        ),
        TeacherLessonItem(
            lessonId = 102,
            date = "27.04.2026".toDate(),
            topic = "Дискриминант",
            startTime = "10:00",
            endTime = "10:45",
            roomName = "42",
            teacherName = "Иванова А.А."
        ),
        TeacherLessonItem(
            lessonId = 103,
            date = "26.04.2026".toDate(),
            topic = "Введение",
            startTime = "08:15",
            endTime = "09:00",
            roomName = "Спортзал",
            teacherName = "Петров Б.Б."
        )
    )

    fun getTeacherClasses(): List<TeacherClassItem> = _teacherClasses
    fun getLessons(classId: Int, subjectId: Int): List<TeacherLessonItem> = _lessons
}