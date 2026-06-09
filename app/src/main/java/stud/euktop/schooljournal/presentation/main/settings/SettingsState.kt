package stud.euktop.schooljournal.presentation.main.settings

import stud.euktop.schooljournal.presentation.common.base.BaseState

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val currentLanguage: String = "Русский",
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<SettingsState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): SettingsState =
        copy(loadingMap = loadingMap)
}