// data/src/main/java/stud/euktop/data/mock/data/MockHomeworkDataSource.kt
package stud.euktop.data.mock.data

import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkMedia
import java.io.File
import java.util.Date

internal object MockHomeworkDataSource {
    private val _storage = mutableListOf<Homework>().apply {
        val teacher = MockUserDataSource.getUser(1)
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

    private val _mockFileContents = mutableMapOf<Int, ByteArray>()

    private val DEFAULT_FILE_BYTES = listOf(
        0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52,
        0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x08, 0x06, 0x00, 0x00, 0x00, 0x1F, 0x15, 0xC4,
        0x89, 0x00, 0x00, 0x00, 0x0A, 0x49, 0x44, 0x41, 0x54, 0x78, 0x9C, 0x63, 0x00, 0x01, 0x00, 0x00,
        0x05, 0x00, 0x01, 0x0D, 0x0A, 0x2D, 0xB4, 0x00, 0x00, 0x00, 0x00, 0x49, 0x45, 0x4E, 0x44, 0xAE,
        0x42, 0x60, 0x82
    ).map { it.toByte() }.toByteArray()

    fun getAll(): List<Homework> = _storage.toList()
    fun getByLesson(lessonId: Int): List<Homework> = _storage.filter { it.lessonId == lessonId }

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
        if (it) {
            _media.remove(homeworkId)
            _media[homeworkId]?.forEach { m -> _mockFileContents.remove(m.mediaId) }
        }
    }

    fun getMedia(homeworkId: Int): List<HomeworkMedia> = _media[homeworkId] ?: emptyList()

    fun addMedia(homeworkId: Int, fileName: String, contentType: String, fileSize: Int): HomeworkMedia {
        val list = _media.getOrPut(homeworkId) { mutableListOf() }
        val newId = (list.maxOfOrNull { it.mediaId } ?: 0) + 1
        val newMedia = HomeworkMedia(
            mediaId = newId,
            fileName = fileName,
            contentType = contentType,
            fileSize = fileSize,
            uploadedAt = Date()
        )
        list.add(newMedia)
        val baseText = "MOCK_FILE_CONTENT_FOR_MEDIA_ID_$newId | File: $fileName | Type: $contentType"
        val baseBytes = baseText.toByteArray(Charsets.UTF_8)
        val content = if (baseBytes.size >= fileSize) {
            baseBytes.copyOf(fileSize)
        } else {
            baseBytes + ByteArray(fileSize - baseBytes.size) { 'M'.code.toByte() }
        }
        _mockFileContents[newId] = content
        return newMedia
    }

    fun deleteMedia(mediaId: Int): Boolean {
        _media.values.forEach { it.removeIf { m -> m.mediaId == mediaId } }
        _mockFileContents.remove(mediaId)
        return true
    }

    fun getById(homeworkId: Int): Homework {
        return _storage.find { it.homeworkId == homeworkId } ?: Homework(
            homeworkId = homeworkId,
            lessonId = 0,
            description = "Домашнее задание не найдено",
            createdAt = Date(),
            medias = emptyList(),
            createdByUserId = 0
        )
    }

    fun getMediaById(mediaId: Int): HomeworkMedia? = _media.values.flatten().find { it.mediaId == mediaId }

    /**
     * Гарантированно возвращает валидный File для тестирования скачивания.
     * Если медиа или контент не найдены, возвращает файл с безопасным PNG по умолчанию,
     * чтобы UI (Coil/Glide) не падал при попытке декодирования.
     */
    fun getMediaFile(mediaId: Int): File {
        val media = getMediaById(mediaId)
        val content = if (media != null) {
            _mockFileContents[mediaId] ?: DEFAULT_FILE_BYTES
        } else {
            DEFAULT_FILE_BYTES
        }
        val extension = media?.fileName?.substringAfterLast('.', "tmp") ?: "png"
        val tempFile = File.createTempFile("mock_download_", ".$extension")
        tempFile.writeBytes(content)
        return tempFile
    }
}