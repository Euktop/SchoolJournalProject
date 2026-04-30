package stud.euktop.schooljournal.presentation.main.teacher.homework

sealed interface TeacherHomeworkEvent {
    object NavigateBack : TeacherHomeworkEvent
    object NavigateToAdd : TeacherHomeworkEvent
    data class EditHomework(val homeworkId: Int) : TeacherHomeworkEvent
}