package stud.euktop.schooljournal.presentation.common.navigate.state

import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationStateManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val mutex = Mutex()

    // === Ключи для последнего посещённого экрана (по ролям) ===
    private fun lastDestKey(role: String) = stringPreferencesKey("last_dest_$role")
    private fun lastArgsKey(role: String) = stringPreferencesKey("last_args_$role")

    // === Ключи для отложенного экрана (ожидающего восстановления после логина) ===
    private val pendingDestKey = stringPreferencesKey("pending_dest")
    private val pendingArgsKey = stringPreferencesKey("pending_args")
    private val pendingRoleKey = stringPreferencesKey("pending_role")

    data class DestinationData(
        val destinationId: Int,
        val arguments: Bundle? = null
    )

    // === Общие методы работы с DataStore ===
    private suspend fun <T> Preferences.Key<T>.getValue(): T? = mutex.withLock {
        dataStore.data.first()[this]
    }

    private suspend fun <T> Preferences.Key<T>.setValue(value: T?) {
        mutex.withLock {
            dataStore.edit { prefs ->
                if (value == null) prefs.remove(this)
                else prefs[this] = value
            }
        }
    }

    // === Работа с последним экраном для конкретной роли ===
    suspend fun saveLastDestination(role: String, destinationData: DestinationData) {
        lastDestKey(role).setValue(destinationData.destinationId.toString())
        val argsStr = destinationData.arguments?.let { bundleToString(it) }
        lastArgsKey(role).setValue(argsStr)
    }

    suspend fun getLastDestination(role: String): DestinationData? {
        val destIdStr = lastDestKey(role).getValue() ?: return null
        val argsStr = lastArgsKey(role).getValue()
        return DestinationData(
            destinationId = destIdStr.toInt(),
            arguments = argsStr?.let { stringToBundle(it) }
        )
    }

    suspend fun clearLastDestination(role: String) {
        lastDestKey(role).setValue(null)
        lastArgsKey(role).setValue(null)
    }

    // === Работа с отложенным экраном (восстановление после логина) ===
    suspend fun savePendingDestination(destinationData: DestinationData, role: String) {
        pendingDestKey.setValue(destinationData.destinationId.toString())
        val argsStr = destinationData.arguments?.let { bundleToString(it) }
        pendingArgsKey.setValue(argsStr)
        pendingRoleKey.setValue(role)
    }

    suspend fun consumePendingDestination(expectedRole: String): DestinationData? {
        val savedRole = pendingRoleKey.getValue()
        if (savedRole != expectedRole) {
            clearPending()
            return null
        }
        val destIdStr = pendingDestKey.getValue() ?: return null
        val argsStr = pendingArgsKey.getValue()
        val data = DestinationData(
            destinationId = destIdStr.toInt(),
            arguments = argsStr?.let { stringToBundle(it) }
        )
        clearPending()
        return data
    }

    private suspend fun clearPending() {
        pendingDestKey.setValue(null)
        pendingArgsKey.setValue(null)
        pendingRoleKey.setValue(null)
    }

    // === Вспомогательная сериализация Bundle (примитивная, можно доработать под реальные типы) ===
    private fun bundleToString(bundle: Bundle): String {
        return bundle.keySet().joinToString("&") { key ->
            val value = bundle.get(key)
            "$key=${value.toString()}"
        }
    }

    private fun stringToBundle(str: String): Bundle {
        val bundle = Bundle()
        str.split("&").forEach { pair ->
            val (key, value) = pair.split("=", limit = 2)
            bundle.putString(key, value)
        }
        return bundle
    }

    suspend fun clearPending(role: String) {
        val savedRole = pendingRoleKey.getValue()
        if (savedRole == role) {
            clearPending()
        }
    }
}