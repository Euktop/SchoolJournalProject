package stud.euktop.data.mock.repository

import stud.euktop.domain.model.settings.Settings
import stud.euktop.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsMockRepositoryImpl @Inject constructor() : SettingsRepository {
    override suspend fun getSettings(): Result<Settings> = Result.success(Settings())
    override suspend fun updateSettings(settings: Settings): Result<Unit> = Result.success(Unit)
    override suspend fun clearCache(): Result<Unit> = Result.success(Unit)
    override suspend fun getAboutAppInfo(): Result<String> = Result.success("Mock School Journal v1.0")
}