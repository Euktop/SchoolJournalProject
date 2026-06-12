package stud.euktop.schooljournal.presentation.main.settings

import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.settings.Settings
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.coordinator.SettingsCoordinator
import stud.euktop.schooljournal.presentation.common.message.MessageEvent
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.uikit.R
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsCoordinator: SettingsCoordinator,
    coordinatorExec: CoordinatorExec
) : BaseViewModel<SettingsState, SettingsEvent>() {

    override fun initState() = SettingsState()

    init {
        executeCoordinator = coordinatorExec
        loadSettings()
    }

    private fun loadSettings() {
        executeCoordinatorResultLoadingBlockSync(
            "load_settings",
            { settingsCoordinator.getSettings() }) { settings ->
            _state.update {
                it.copy(
                    isDarkTheme = settings.isDarkTheme,
                    isNotificationsEnabled = settings.isNotificationsEnabled,
                    currentLanguage = settings.currentLanguage
                )
            }
        }
    }

    fun toggleDarkTheme(enabled: Boolean) {
        _state.update { it.copy(isDarkTheme = enabled) }
        saveSettings()
    }

    fun toggleNotifications(enabled: Boolean) {
        _state.update { it.copy(isNotificationsEnabled = enabled) }
        saveSettings()
    }

    fun changeLanguage(language: String) {
        _state.update { it.copy(currentLanguage = language) }
        saveSettings()
    }

    private fun applyTheme(isDark: Boolean) {
        val mode = if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun saveSettings() {
        val state = _state.value
        val settings = Settings(
            isDarkTheme = state.isDarkTheme,
            isNotificationsEnabled = state.isNotificationsEnabled,
            currentLanguage = state.currentLanguage
        )
        applyTheme(state.isDarkTheme)
        executeCoordinatorResultLoadingBlockSync(
            "save_settings",
            { settingsCoordinator.updateSettings(settings) }) { }
    }

    fun openAboutApp() {
        executeCoordinatorResultLoadingBlockSync(
            "about_app",
            { settingsCoordinator.getAboutAppInfo() }
        ) { info ->
            _event.tryEmit(SettingsEvent.ShowAboutDialog(info))
        }
    }

    fun clearCache() {
        executeCoordinatorResultLoadingBlockSync(
            "clear_cache",
            { settingsCoordinator.clearCache() }
        ) {
            _messageEvent.tryEmit(MessageEvent.Message(MessageParam(R.string.cache_cleared)))
        }
    }
}