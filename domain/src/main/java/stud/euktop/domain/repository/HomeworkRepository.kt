// domain/src/main/java/stud/euktop/domain/repository/HomeworkRepository.kt
package stud.euktop.domain.repository

import stud.euktop.domain.model.homework.Homework

interface HomeworkRepository {
    /**
     * Получить все домашние задания, созданные учителем (по ID учителя).
     * @param teacherId ID пользователя-учителя.
     */
    suspend fun getHomeworkByTeacher(teacherId: Int): Result<List<Homework>>

    /**
     * Получить домашние задания, доступные ученику (через уроки его класса).
     * @param studentId ID ученика.
     */
    suspend fun getHomeworkByStudent(studentId: Int): Result<List<Homework>>

    /**
     * Получить домашние задания, привязанные к конкретному уроку.
     * @param lessonId ID урока.
     */
    suspend fun getHomeworkByLesson(lessonId: Int): Result<List<Homework>>

    /**
     * Добавить новое домашнее задание.
     * @param homework ДЗ (без homeworkId, создаётся автоматически).
     */
    suspend fun addHomework(homework: Homework): Result<Homework>

    /**
     * Обновить существующее домашнее задание.
     */
    suspend fun updateHomework(homework: Homework): Result<Homework>

    /**
     * Удалить домашнее задание.
     */
    suspend fun deleteHomework(homeworkId: Int): Result<Unit>
}