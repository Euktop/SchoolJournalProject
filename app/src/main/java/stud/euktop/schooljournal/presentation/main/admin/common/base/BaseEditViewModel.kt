package stud.euktop.schooljournal.presentation.main.admin.common.base

import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager

/**
 * Базовый ViewModel для экранов редактирования.
 * Предоставляет единый способ навигации назад и общую обработку загрузки.
 */
abstract class BaseEditViewModel<STATE : BaseState<STATE>>(
    coordinatorExec: CoordinatorExec,
    navigationManager: NavigationManager
) : BaseViewModel<STATE, BaseEditEvent>() {
    init {
        executeCoordinator = ExecuteCoordinator(coordinatorExec, navigationManager)
    }

    /**
     * Выполняет навигацию назад (закрывает экран редактирования).
     */
    protected fun navigateBack() {
        _event.tryEmit(BaseEditEvent.NavigateBack)
    }
}