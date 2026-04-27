package stud.euktop.schooljournal.presentation.common.base

abstract class BaseState<T : BaseState<T>> {
    abstract val isLoading: Boolean
    abstract fun updateIsLoading(isLoading: Boolean): T
}