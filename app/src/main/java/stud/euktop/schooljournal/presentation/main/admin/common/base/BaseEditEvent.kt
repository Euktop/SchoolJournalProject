package stud.euktop.schooljournal.presentation.main.admin.common.base

sealed interface BaseEditEvent {
    object NavigateBack : BaseEditEvent
}