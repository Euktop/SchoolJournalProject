// data/src/main/java/stud/euktop/data/mock/MockHomeworkDataSource.kt
package stud.euktop.data.mock

import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.school.ClassInfo
import java.util.Date

object MockHomeworkDataSource {
    private val _storage = mutableListOf<Homework>().apply {
        // Инициализация тестовыми данными (если нужны)
        val teacher = MockUserDataSource.getUser(1) // учитель Петрова
        val lesson = MockLessonDataSource.getLesson(101) // должен существовать
        if (teacher != null && lesson != null) {
            add(
                Homework(
                    homeworkId = 1,
                    lesson = lesson,
                    description = "Решить уравнения №1-10",
                    attachedFiles = null,
                    createdAt = Date(),
                    createdByUser = teacher
                )
            )
            add(
                Homework(
                    homeworkId = 2,
                    lesson = lesson,
                    description = "Прочитать параграф 5",
                    attachedFiles = null,
                    createdAt = Date(),
                    createdByUser = teacher
                )
            )
        }
    }

    fun getAll(): List<Homework> = _storage.toList()

    fun getByTeacher(teacherId: Int): List<Homework> =
        _storage.filter { it.createdByUser.userId == teacherId }

    fun getByStudent(studentId: Int): List<Homework> {
        // Получаем класс ученика (есть ли такой метод в MockClassDataSource?)
        val studentClass =
            MockClassDataSource.getClassByStudent(studentId) // предположим, что добавим
        return if (studentClass != null) {
            _storage.filter { it.lesson.classInfo.classId == studentClass.classId }
        } else emptyList()
    }

    fun getByLesson(lessonId: Int): List<Homework> =
        _storage.filter { it.lesson.lessonId == lessonId }

    fun add(homework: Homework): Homework {
        val newId = (_storage.maxOfOrNull { it.homeworkId } ?: 0) + 1
        val newHomework = homework.copy(homeworkId = newId, createdAt = Date())
        _storage.add(newHomework)
        return newHomework
    }

    fun update(homework: Homework) {
        val index = _storage.indexOfFirst { it.homeworkId == homework.homeworkId }
        if (index >= 0) _storage[index] = homework
    }

    fun delete(homeworkId: Int): Boolean = _storage.removeIf { it.homeworkId == homeworkId }

    fun getClassByStudent(studentId: Int): ClassInfo? {
        val student = MockUserDataSource.getUser(studentId)
        return if (student != null && student.roles.any { it.role == Role.STUDENT }) {
            getAll().firstOrNull()?.lesson?.classInfo
        } else null
    }
}