package stud.euktop.data.mock.repository

import stud.euktop.data.local.storage.contract.SettingsStorage
import stud.euktop.data.mock.data.MockDelayService
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.settings.Settings
import stud.euktop.domain.repository.SettingsRepository
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsMockRepositoryImpl @Inject constructor(
    private val settingsStorage: SettingsStorage,
    private val apiErrorHandler: ApiErrorHandler
) : SettingsRepository {
    private val tag = this.toSimpleTag()

    override suspend fun getSettings(): Result<Settings> = apiErrorHandler.safeApiCall {
        logger?.d(tag, "getSettings", "Fetching settings from storage")
        MockDelayService.delay()
        val settings = settingsStorage.getSettings()
        logger?.i(
            tag,
            "getSettings_success",
            "isDark=${settings.isDarkTheme}, lang=${settings.currentLanguage}"
        )
        settings
    }

    override suspend fun updateSettings(settings: Settings): Result<Unit> =
        apiErrorHandler.safeApiCall {
            logger?.i(tag, "updateSettings", "Updating settings to: $settings")
            MockDelayService.delay()
            settingsStorage.saveSettings(settings)
            logger?.i(tag, "updateSettings_success", "Settings saved successfully")
            Unit
        }

    override suspend fun clearCache(): Result<Unit> = apiErrorHandler.safeApiCall {
        logger?.i(tag, "clearCache", "Clearing settings cache")
        MockDelayService.delay()
        settingsStorage.clearSettings()
        logger?.i(tag, "clearCache_success", "Cache cleared")
        Unit
    }

    override suspend fun getAboutAppInfo(): Result<String> = apiErrorHandler.safeApiCall {
        logger?.d(tag, "getAboutAppInfo", "Returning app info")
        MockDelayService.delay()
        "Mock School Journal v1.0 (Debug Build)"
    }
}