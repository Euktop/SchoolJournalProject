package stud.euktop.data.local.storage.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import stud.euktop.data.local.storage.contract.RoleStorage
import stud.euktop.data.local.storage.contract.TokenStorage
import stud.euktop.data.local.storage.contract.UserIdStorage
import stud.euktop.domain.model.user.Role
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("session_prefs")

@Singleton
class SessionStorageImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : TokenStorage, UserIdStorage, RoleStorage {

    private val tokenKey = stringPreferencesKey("token")
    private val userIdKey = intPreferencesKey("user_id")
    private val roleKey = intPreferencesKey("role_id")
    private val mutex = Mutex()

    private suspend fun <T> Preferences.Key<T>.getValue(): T? = mutex.withLock {
        context.dataStore.data.first()[this]
    }

    private suspend fun <T> Preferences.Key<T>.setValue(value: T?) {
        mutex.withLock {
            context.dataStore.edit { preferences ->
                if (value == null) {
                    preferences.remove(this)
                } else {
                    preferences[this] = value
                }
            }
        }
    }

    override suspend fun getToken(): String? = tokenKey.getValue()
    override suspend fun setToken(token: String?) = tokenKey.setValue(token)

    override suspend fun getUserId(): Int? = userIdKey.getValue()
    override suspend fun setUserId(userId: Int?) = userIdKey.setValue(userId)

    override suspend fun clearAll() {
        mutex.withLock {
            context.dataStore.edit { it.clear() }
        }
    }

    override suspend fun getRole() =
        roleKey.getValue()?.let {
            Role.entries.getOrNull(it)
        }

    override suspend fun saveRole(role: Role?) = roleKey.setValue(role?.ordinal)
}