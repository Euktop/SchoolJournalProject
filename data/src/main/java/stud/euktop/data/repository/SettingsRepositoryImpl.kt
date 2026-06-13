package stud.euktop.data.repository

import com.schooljournal.api.SystemApi
import stud.euktop.data.local.storage.contract.SettingsStorage
import stud.euktop.data.map.toAboutInfo
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.settings.Settings
import stud.euktop.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsStorage: SettingsStorage,
    private val systemApi: SystemApi,
    private val errorHandler: ApiErrorHandler
) : SettingsRepository {

    override suspend fun getSettings(): Result<Settings> = runCatching {
        settingsStorage.getSettings()
    }

    override suspend fun updateSettings(settings: Settings): Result<Unit> = runCatching {
        settingsStorage.saveSettings(settings)
    }

    override suspend fun clearCache(): Result<Unit> = runCatching {
        settingsStorage.clearSettings()
    }

    override suspend fun getAboutAppInfo(): Result<String> = errorHandler.safeApiCall {
        systemApi.apiSystemInfoGet().toAboutInfo()
    }
}