// presentation/main/admin/classes/ClassEditEvent.kt
package stud.euktop.schooljournal.presentation.main.admin.classes

sealed interface ClassEditEvent {
    object NavigateBack : ClassEditEvent
}