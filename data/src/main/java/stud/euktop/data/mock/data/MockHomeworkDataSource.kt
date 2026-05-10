// data/src/main/java/stud/euktop/data/mock/data/MockHomeworkDataSource.kt
package stud.euktop.data.mock.data

import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkMedia
import java.util.Date

internal object MockHomeworkDataSource {
    private val _storage = mutableListOf<Homework>().apply {
        val teacher = MockUserDataSource.getUser(1) // учитель Иванов
        val lesson = MockLessonDataSource.getLesson(101)
        if (teacher != null && lesson != null) {
            add(
                Homework(
                    homeworkId = 1,
                    lessonId = lesson.lessonId,
                    description = "Решить уравнения №1-10",
                    createdAt = Date(),
                    medias = emptyList(),
                    createdByUserId = teacher.userId
                )
            )
            add(
                Homework(
                    homeworkId = 2,
                    lessonId = lesson.lessonId,
                    description = "Прочитать параграф 5",
                    createdAt = Date(),
                    medias = emptyList(),
                    createdByUserId = teacher.userId
                )
            )
        }
    }

    private val _media = mutableMapOf<Int, MutableList<HomeworkMedia>>().apply {
        put(1, mutableListOf())
        put(2, mutableListOf())
    }

    fun getAll(): List<Homework> = _storage.toList()
    fun getByLesson(lessonId: Int): List<Homework> = _storage.filter { it.lessonId == lessonId }
    fun getById(homeworkId: Int): Homework? = _storage.find { it.homeworkId == homeworkId }

    fun add(homework: Homework): Homework {
        val newId = (_storage.maxOfOrNull { it.homeworkId } ?: 0) + 1
        val newHomework = homework.copy(homeworkId = newId, createdAt = Date(), medias = emptyList())
        _storage.add(newHomework)
        _media[newId] = mutableListOf()
        return newHomework
    }

    fun update(homework: Homework) {
        val index = _storage.indexOfFirst { it.homeworkId == homework.homeworkId }
        if (index >= 0) _storage[index] = homework
    }

    fun delete(homeworkId: Int): Boolean = _storage.removeIf { it.homeworkId == homeworkId }.also {
        if (it) _media.remove(homeworkId)
    }

    fun getMedia(homeworkId: Int): List<HomeworkMedia> = _media[homeworkId] ?: emptyList()
    fun addMedia(homeworkId: Int, media: HomeworkMedia): HomeworkMedia {
        val list = _media.getOrPut(homeworkId) { mutableListOf() }
        val newId = (list.maxOfOrNull { it.mediaId } ?: 0) + 1
        val newMedia = media.copy(mediaId = newId, uploadedAt = Date())
        list.add(newMedia)
        return newMedia
    }
    fun deleteMedia(mediaId: Int): Boolean {
        _media.values.forEach { it.removeIf { m -> m.mediaId == mediaId } }
        return true
    }
    fun getMediaById(mediaId: Int): HomeworkMedia? = _media.values.flatten().find { it.mediaId == mediaId }
}