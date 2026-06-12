package stud.euktop.data.repository

import stud.euktop.domain.model.settings.Settings
import stud.euktop.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    // TODO: реализовать через DataStore
    override suspend fun getSettings(): Result<Settings> = Result.success(Settings())
    override suspend fun updateSettings(settings: Settings): Result<Unit> = Result.success(Unit)
    override suspend fun clearCache(): Result<Unit> = Result.success(Unit)
    override suspend fun getAboutAppInfo(): Result<String> = Result.success("School Journal v1.0")
}