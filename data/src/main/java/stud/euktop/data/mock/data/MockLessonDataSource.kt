// data/src/main/java/stud/euktop/data/mock/data/MockLessonDataSource.kt
package stud.euktop.data.mock.data

import stud.euktop.domain.model.lesson.Lesson
import java.util.Date

internal object MockLessonDataSource {
    private val _lessons = mutableListOf<Lesson>().apply {
        // ID существующих объектов
        val class5AId = 1
        val class5BId = 2
        val mathId = 1
        val russianId = 2
        val physicsId = 3
        val teacherIvanovId = 1
        val teacherPetrovaId = 2
        val room101Id = 1
        val room102Id = 1 // тот же кабинет
        val room103Id = 2

        add(
            Lesson(
                lessonId = 101,
                classId = class5AId,
                subjectId = mathId,
                teacherId = teacherPetrovaId,
                date = Date(),
                topic = "Квадратные уравнения",
                startTime = "09:00",
                endTime = "09:45",
                roomId = room101Id,
                locationAddress = null
            )
        )
        add(
            Lesson(
                lessonId = 102,
                classId = class5AId,
                subjectId = russianId,
                teacherId = teacherIvanovId,
                date = Date(),
                topic = "Дискриминант",
                startTime = "10:00",
                endTime = "10:45",
                roomId = room102Id,
                locationAddress = null
            )
        )
        add(
            Lesson(
                lessonId = 103,
                classId = class5BId,
                subjectId = physicsId,
                teacherId = teacherPetrovaId,
                date = Date(),
                topic = "Введение",
                startTime = "08:15",
                endTime = "09:00",
                roomId = room103Id,
                locationAddress = null
            )
        )
    }

    fun getAll(): List<Lesson> = _lessons.toList()
    fun getLesson(lessonId: Int): Lesson? = _lessons.find { it.lessonId == lessonId }
        ?: _lessons.firstOrNull() ?: Lesson(
            lessonId = lessonId,
            classId = 0,
            subjectId = 0,
            teacherId = 0,
            date = java.util.Date(),
            topic = "Неизвестный урок",
            startTime = "00:00",
            endTime = "00:00",
            roomId = null,
            locationAddress = null
        )
    fun addLesson(lesson: Lesson): Lesson {
        val newId = (_lessons.maxOfOrNull { it.lessonId } ?: 0) + 1
        val newLesson = lesson.copy(lessonId = newId)
        _lessons.add(newLesson)
        return newLesson
    }
    fun updateLesson(lesson: Lesson) {
        val index = _lessons.indexOfFirst { it.lessonId == lesson.lessonId }
        if (index >= 0) _lessons[index] = lesson
    }
    fun deleteLesson(lessonId: Int): Boolean = _lessons.removeIf { it.lessonId == lessonId }
    fun getLessonsByClass(classId: Int): List<Lesson> = _lessons.filter { it.classId == classId }
}