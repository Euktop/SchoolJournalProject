package stud.euktop.schooljournal.presentation.main.student.homework

import stud.euktop.domain.model.homework.HomeworkFull
import java.io.File

sealed class StudentHomeworkEvent {
    data class ShowHomeworkDetail(val homework: HomeworkFull) : StudentHomeworkEvent()
    data class DownloadMediaFile(val file: File) : StudentHomeworkEvent()
}