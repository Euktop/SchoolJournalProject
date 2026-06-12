package stud.euktop.schooljournal.presentation.common.navigate

import androidx.annotation.StringRes

sealed interface CoordinatorResult<out T> {
    fun getOrNull(): T?
    data class Success<T>(
        val result: T
    ) : CoordinatorResult<T> {
        override fun getOrNull(): T? {
            return result
        }

    }

    data class Error(
        val navAction: suspend () -> Unit,
        @param:StringRes val messageId: Int
    ) : CoordinatorResult<Nothing> {
        override fun getOrNull(): Nothing? {
            return null
        }
    }
}