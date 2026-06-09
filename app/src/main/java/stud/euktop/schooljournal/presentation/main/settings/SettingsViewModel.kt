package stud.euktop.schooljournal.presentation.main.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    coordinatorExec: CoordinatorExec
) : BaseViewModel<SettingsState, Unit>() {

    override fun initState() = SettingsState()

    init {
        executeCoordinator = coordinatorExec
        // TODO: Загрузить настройки из DataStore/Preferences
    }

    fun toggleDarkTheme(enabled: Boolean) {
        _state.update { it.copy(isDarkTheme = enabled) }
        // TODO: Сохранить в DataStore и применить AppCompatDelegate.setDefaultNightMode()
    }

    fun toggleNotifications(enabled: Boolean) {
        _state.update { it.copy(isNotificationsEnabled = enabled) }
        // TODO: Сохранить в DataStore
    }

    fun clearCache() {
        // TODO: Реализовать очистку кэша
    }

    fun openAboutApp() {
        // TODO: Открыть диалог "О приложении"
    }
}