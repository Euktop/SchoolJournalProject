package stud.euktop.data.local.storage.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import stud.euktop.data.local.storage.contract.SettingsStorage
import stud.euktop.domain.model.settings.Settings
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore("settings_prefs")

@Singleton
class SettingsStorageImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : SettingsStorage {

    private val darkThemeKey = booleanPreferencesKey("is_dark_theme")
    private val notificationsKey = booleanPreferencesKey("is_notifications_enabled")
    private val languageKey = stringPreferencesKey("current_language")
    private val mutex = Mutex()

    private suspend fun <T> Preferences.Key<T>.getValue(default: T): T = mutex.withLock {
        context.settingsDataStore.data.first()[this] ?: default
    }

    private suspend fun <T> Preferences.Key<T>.setValue(value: T) {
        mutex.withLock {
            context.settingsDataStore.edit { preferences ->
                preferences[this] = value
            }
        }
    }

    override suspend fun getSettings(): Settings {
        return Settings(
            isDarkTheme = darkThemeKey.getValue(false),
            isNotificationsEnabled = notificationsKey.getValue(true),
            currentLanguage = languageKey.getValue("Русский")
        )
    }

    override suspend fun saveSettings(settings: Settings) {
        mutex.withLock {
            context.settingsDataStore.edit { preferences ->
                preferences[darkThemeKey] = settings.isDarkTheme
                preferences[notificationsKey] = settings.isNotificationsEnabled
                preferences[languageKey] = settings.currentLanguage
            }
        }
    }

    override suspend fun clearSettings() {
        mutex.withLock {
            context.settingsDataStore.edit { it.clear() }
        }
    }
}