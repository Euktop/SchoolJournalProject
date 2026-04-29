package stud.euktop.schooljournal.presentation.auth.login

sealed interface LoginEvent {
    object NavigateToMain : LoginEvent
}