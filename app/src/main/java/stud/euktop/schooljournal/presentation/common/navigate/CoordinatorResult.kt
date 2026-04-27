package stud.euktop.schooljournal.presentation.common.navigate

import androidx.annotation.StringRes

sealed interface CoordinatorResult<out T> {
    data class Success<T>(
        val result: T
    ) : CoordinatorResult<T>

    data class Error(
        val navCommand: NavCommand,
        @param: StringRes val messageId: Int
    ) : CoordinatorResult<Nothing>
}