package stud.euktop.domain.model.homework

import java.util.Date

data class HomeworkMedia(
    val mediaId: Int,
    val fileName: String,
    val contentType: String,
    val fileSize: Int,
    val uploadedAt: Date
)