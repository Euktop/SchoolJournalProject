// presentation/main/admin/subjects/SubjectEditEvent.kt
package stud.euktop.schooljournal.presentation.main.admin.subjects

sealed interface SubjectEditEvent {
    object NavigateBack : SubjectEditEvent
}