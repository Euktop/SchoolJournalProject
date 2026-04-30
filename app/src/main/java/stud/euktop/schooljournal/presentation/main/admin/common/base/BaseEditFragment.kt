package stud.euktop.schooljournal.presentation.main.admin.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager

/**
 * Базовый фрагмент для экранов редактирования.
 * Автоматически обрабатывает событие NavigateBack (закрытие экрана через NavigationManager).
 *
 * @param VM ViewModel, наследующий BaseEditViewModel
 * @param STATE состояние редактирования
 */
abstract class BaseEditFragment<BINDING : ViewBinding, VM : BaseEditViewModel<STATE>, STATE : BaseState<STATE>> :
    BaseFragment<BINDING, VM, STATE, BaseEditEvent>() {

    abstract var navigationManager: NavigationManager
    override fun updateEvent(event: BaseEditEvent) {
        when (event) {
            BaseEditEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }
}