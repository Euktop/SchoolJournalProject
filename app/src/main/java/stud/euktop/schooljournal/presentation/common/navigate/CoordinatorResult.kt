package stud.euktop.schooljournal.presentation.common.navigate

sealed interface CoordinatorResult<out T> {
    data class Success<T>(
        val result: T
    ) : CoordinatorResult<T>

    data class Error(
        val error: Throwable
    ) : CoordinatorResult<Nothing>
}