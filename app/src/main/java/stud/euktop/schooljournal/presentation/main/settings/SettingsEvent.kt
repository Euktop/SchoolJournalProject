package stud.euktop.schooljournal.presentation.main.settings

sealed class SettingsEvent {
    data class ShowAboutDialog(val info: String) : SettingsEvent()
}