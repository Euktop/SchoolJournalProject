// domain/src/main/java/stud/euktop/domain/repository/HomeworkRepository.kt
package stud.euktop.domain.repository

import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.model.homework.HomeworkMedia
import stud.euktop.domain.model.homework.HomeworkUpdate
import java.io.File

interface HomeworkRepository {
    suspend fun getHomeworks(filter: HomeworkFilter): Result<List<Homework>>
    suspend fun getHomeworkById(id: Int): Result<Homework>
    suspend fun getHomeworkFullById(id: Int): Result<HomeworkFull>
    suspend fun addHomework(homework: Homework): Result<Homework>
    suspend fun updateHomework(update: HomeworkUpdate): Result<Homework>
    suspend fun deleteHomework(homeworkId: Int): Result<Unit>

    suspend fun getMediaList(homeworkId: Int): Result<List<HomeworkMedia>>
    suspend fun addMedia(homeworkId: Int, file: File): Result<HomeworkMedia>
    suspend fun deleteMedia(mediaId: Int): Result<Unit>
    suspend fun downloadMedia(mediaId: Int): Result<File>
}