package stud.euktop.schooljournal.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.message.MessageEvent
import stud.euktop.schooljournal.presentation.common.message.impl.SnackBarMessages
import stud.euktop.schooljournal.presentation.common.message.contract.MessageDisplayer

abstract class BaseFragment<BINDING : ViewBinding, VM : BaseViewModel<STATE, EVENT>, STATE : BaseState<STATE>, EVENT : Any> :
    Fragment() {
    protected lateinit var binding: BINDING
    protected abstract fun inflateBinding(i: LayoutInflater, c: ViewGroup?): BINDING
    protected abstract val viewModel: VM
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateBinding(inflater, container)
        messages = SnackBarMessages(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.isLoading)
                    updateLoading(it)
                else
                    updateState(it)
            }
        }
        lifecycleScope.launch {
            viewModel.event.collect { event ->
                updateEvent(event)
            }
        }
        lifecycleScope.launch {
            viewModel.messageEvent.collect { event ->
                updateMessageEvent(event)
            }
        }
    }

    protected open fun updateLoading(state: STATE) {}

    protected abstract fun setupUI()
    protected abstract fun updateState(state: STATE = viewModel.state.value)
    protected lateinit var messages: MessageDisplayer
    protected open fun updateMessageEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.Message -> messages.message(event.param)
        }
    }

    protected abstract fun updateEvent(event: EVENT)
}