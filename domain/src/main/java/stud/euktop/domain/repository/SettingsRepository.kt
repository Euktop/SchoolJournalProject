package stud.euktop.domain.repository

import stud.euktop.domain.model.settings.Settings

interface SettingsRepository {
    suspend fun getSettings(): Result<Settings>
    suspend fun updateSettings(settings: Settings): Result<Unit>
    suspend fun clearCache(): Result<Unit>
    suspend fun getAboutAppInfo(): Result<String> // или Any
}