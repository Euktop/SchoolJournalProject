package stud.euktop.schooljournal.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import stud.euktop.schooljournal.presentation.common.delegate.BindingDelegate
import stud.euktop.schooljournal.presentation.common.message.contract.MessageDisplayer
import stud.euktop.schooljournal.presentation.common.message.impl.SnackBarMessages
import stud.euktop.schooljournal.presentation.common.utils.observeEvent
import stud.euktop.schooljournal.presentation.common.utils.observeMessage
import stud.euktop.schooljournal.presentation.common.utils.observeState

/**
 * Базовый фрагмент с поддержкой делегатов:
 * - BindingDelegate – управление ViewBinding
 * - MessageDelegate – автоматический показ SnackBar из messageEvent
 *
 * Подписка на state и event остаётся во фрагменте (через observeState/observeEvent расширения),
 * чтобы не усложнять абстракциями.
 *
 * Наследники должны:
 * - реализовать inflateBinding() – создание ViewBinding
 * - реализовать setupUI() – настройка UI после создания view
 * - реализовать updateState() и updateEvent()
 */
abstract class BaseFragment<BINDING : ViewBinding, VM : BaseViewModel<STATE, EVENT>, STATE : BaseState<STATE>, EVENT : Any> :
    Fragment() {

    protected abstract val viewModel: VM
    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): BINDING
    protected abstract fun setupUI()
    protected abstract fun updateState(state: STATE)
    protected open fun updateEvent(event: EVENT) {}

    private lateinit var bindingDelegate: BindingDelegate<BINDING>

    protected val binding: BINDING
        get() = bindingDelegate.binding

    protected lateinit var messages: MessageDisplayer
        private set

    // ---------- Жизненный цикл ----------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingDelegate = BindingDelegate { inflater, container, _ ->
            inflateBinding(inflater, container)
        }
        bindingDelegate.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        messages = SnackBarMessages(binding.root, lifecycleScope)
        observeMessage(viewModel, messages)
        observeViewModel()
    }

    override fun onDestroyView() {
        bindingDelegate.onDestroyView()
        super.onDestroyView()
    }

    private fun observeViewModel() {
        observeState(viewModel.state) {
            updateState(it)
        }
        observeEvent(viewModel.event) {
            updateEvent(it)
        }
    }
}