// data/src/main/java/stud/euktop/data/mock/MockLessonDataSource.kt
package stud.euktop.data.mock

import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserInfo
import java.util.Date

object MockLessonDataSource {
    // Хранилище уроков (полные объекты Lesson)
    private val _lessons = mutableListOf<Lesson>().apply {
        // Получаем существующие данные из других моков
        val class5A: ClassInfo? = MockClassDataSource.get(1)
        val class5B: ClassInfo? = MockClassDataSource.get(2)
        val math: Subject? = MockSubjectDataSource.get(1)
        val russian: Subject? = MockSubjectDataSource.get(2)
        val physics: Subject? = MockSubjectDataSource.get(3)
        val teacherIvanova: UserInfo? = MockUserDataSource.getUser(1) // Иванов
        val teacherPetrova: UserInfo? = MockUserDataSource.getUser(2) // Петрова

        // Урок математики в 5А
        if (class5A != null && math != null && teacherPetrova != null) {
            add(
                Lesson(
                    lessonId = 101,
                    classInfo = class5A,
                    subject = math,
                    teacher = teacherPetrova,
                    date = Date(),
                    topic = "Квадратные уравнения",
                    startTime = "09:00",
                    endTime = "09:45",
                    room = null,
                    locationAddress = null
                )
            )
        }
        // Урок русского языка в 5А
        if (class5A != null && russian != null && teacherIvanova != null) {
            add(
                Lesson(
                    lessonId = 102,
                    classInfo = class5A,
                    subject = russian,
                    teacher = teacherIvanova,
                    date = Date(),
                    topic = "Дискриминант",
                    startTime = "10:00",
                    endTime = "10:45",
                    room = null,
                    locationAddress = null
                )
            )
        }
        // Урок физики в 5Б
        if (class5B != null && physics != null && teacherPetrova != null) {
            add(
                Lesson(
                    lessonId = 103,
                    classInfo = class5B,
                    subject = physics,
                    teacher = teacherPetrova,
                    date = Date(),
                    topic = "Введение",
                    startTime = "08:15",
                    endTime = "09:00",
                    room = null,
                    locationAddress = null
                )
            )
        }
    }

    fun getAll(): List<Lesson> = _lessons.toList()

    fun getLesson(lessonId: Int): Lesson? = _lessons.find { it.lessonId == lessonId }

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
}