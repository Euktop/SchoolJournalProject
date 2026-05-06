package stud.euktop.schooljournal.presentation.main.admin.lessons

sealed interface LessonEditEvent {
    object NavigateBack : LessonEditEvent
}