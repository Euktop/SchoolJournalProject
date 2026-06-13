package stud.euktop.data.repository

import stud.euktop.data.local.storage.contract.SettingsStorage
import stud.euktop.domain.model.settings.Settings
import stud.euktop.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsStorage: SettingsStorage
) : SettingsRepository {

    override suspend fun getSettings(): Result<Settings> {
        return runCatching {
            settingsStorage.getSettings()
        }
    }

    override suspend fun updateSettings(settings: Settings): Result<Unit> {
        return runCatching {
            settingsStorage.saveSettings(settings)
        }
    }

    override suspend fun clearCache(): Result<Unit> {
        return runCatching {
            settingsStorage.clearSettings()
        }
    }

    override suspend fun getAboutAppInfo(): Result<String> {
        return Result.success("School Journal v1.0")
    }
}