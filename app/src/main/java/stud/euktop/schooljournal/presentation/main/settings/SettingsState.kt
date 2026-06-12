package stud.euktop.schooljournal.presentation.main.settings

import stud.euktop.schooljournal.presentation.common.base.BaseState

/**
 * Состояние экрана настроек.
 * @param isDarkTheme тёмная тема
 * @param isNotificationsEnabled уведомления
 * @param currentLanguage текущий язык (получен из настроек или значение по умолчанию)
 */
data class SettingsState(
    val isDarkTheme: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val currentLanguage: String = DEFAULT_LANGUAGE,
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<SettingsState>() {
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): SettingsState =
        copy(loadingMap = loadingMap)

    companion object {
        // Константа для значения по умолчанию (будет перезаписана из сохранённых настроек)
        const val DEFAULT_LANGUAGE = "Русский"
    }
}