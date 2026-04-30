package stud.euktop.domain.model.homework

import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.lesson.Lesson
import java.util.Date

/**
 * Домашнее задание.
 *
 * @property homeworkId уникальный идентификатор (0 для новых записей)
 * @property lessonId идентификатор урока, к которому привязано ДЗ
 * @property description описание задания (может быть HTML или текст)
 * @property attachedFiles URL-ссылки на прикреплённые файлы (например, JSON-массив строк)
 * @property createdAt дата и время создания
 * @property createdByUserId идентификатор пользователя (учителя), создавшего ДЗ
 */
data class Homework(
    val homeworkId: Int = 0,
    val lesson: Lesson,
    val description: String,
    val attachedFiles: String? = null,
    val createdAt: Date,
    val createdByUser: UserInfo
)