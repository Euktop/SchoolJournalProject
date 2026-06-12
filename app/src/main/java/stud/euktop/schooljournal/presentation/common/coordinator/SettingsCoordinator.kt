package stud.euktop.schooljournal.presentation.common.coordinator

import stud.euktop.domain.model.settings.Settings
import stud.euktop.domain.repository.SettingsRepository
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsCoordinator @Inject constructor(
    private val repository: SettingsRepository,
    private val coordinatorExec: CoordinatorExec
) {
    suspend fun getSettings(): CoordinatorResult<Settings> =
        coordinatorExec.exec { repository.getSettings() }

    suspend fun updateSettings(settings: Settings): CoordinatorResult<Unit> =
        coordinatorExec.exec { repository.updateSettings(settings) }

    suspend fun clearCache(): CoordinatorResult<Unit> =
        coordinatorExec.exec { repository.clearCache() }

    suspend fun getAboutAppInfo(): CoordinatorResult<String> =
        coordinatorExec.exec { repository.getAboutAppInfo() }
}