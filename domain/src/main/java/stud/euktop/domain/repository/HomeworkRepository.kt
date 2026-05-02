// domain/src/main/java/stud/euktop/domain/repository/HomeworkRepository.kt
package stud.euktop.domain.repository

import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFilter

interface HomeworkRepository {

    suspend fun getHomeworks(filter: HomeworkFilter = HomeworkFilter()): Result<List<Homework>>
    suspend fun getHomeworkById(id: Int): Result<Homework>

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