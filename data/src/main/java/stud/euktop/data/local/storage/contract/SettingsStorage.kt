package stud.euktop.data.local.storage.contract

import stud.euktop.domain.model.settings.Settings

interface SettingsStorage {
    suspend fun getSettings(): Settings
    suspend fun saveSettings(settings: Settings)
    suspend fun clearSettings()
}