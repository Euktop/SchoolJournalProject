package stud.euktop.schooljournal.presentation.main.admin.users

sealed interface UserEditEvent {
    object NavigateBack : UserEditEvent
}