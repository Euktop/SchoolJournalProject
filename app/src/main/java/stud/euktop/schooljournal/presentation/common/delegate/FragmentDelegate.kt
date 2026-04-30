package stud.euktop.schooljournal.presentation.common.delegate

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.message.MessageEvent
import stud.euktop.schooljournal.presentation.common.message.contract.MessageDisplayer
import stud.euktop.schooljournal.presentation.common.message.impl.SnackBarMessages
import stud.euktop.uikit.R

class FragmentDelegate<BINDING : ViewBinding, VM : BaseViewModel<STATE, EVENT>, STATE : BaseState<STATE>, EVENT : Any>(
    private val fragment: Fragment,
    private val viewModelLazy: Lazy<VM>,
    private val inflateBinding: (LayoutInflater, ViewGroup?) -> BINDING,
    private val onStateUpdated: (STATE) -> Unit,
    private val onEventReceived: (EVENT) -> Unit,
    private val setupUI: (BINDING, VM, FragmentDelegate<BINDING, VM, STATE, EVENT>) -> Unit = { _, _, _ -> }
) {
    private var _binding: BINDING? = null
    val binding: BINDING get() = _binding!!
    private var messageDisplayer: MessageDisplayer? = null
    private val lifecycleOwner = fragment.viewLifecycleOwnerLiveData

    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        messageDisplayer = SnackBarMessages(binding.root)
        return binding.root
    }

    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm = viewModelLazy.value
        setupUI(binding, vm, this)
        observeViewModel(vm)
    }

    private fun observeViewModel(vm: VM) {
        fragment.viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                if (!state.isLoading) onStateUpdated(state)
                // загрузку можно показывать через showLoadingOverlay(state.isLoading)
            }
        }
        fragment.viewLifecycleOwner.lifecycleScope.launch {
            vm.event.collect { event -> onEventReceived(event) }
        }
        fragment.viewLifecycleOwner.lifecycleScope.launch {
            vm.messageEvent.collect { event ->
                when (event) {
                    is MessageEvent.Message -> messageDisplayer?.message(event.param)
                }
            }
        }
    }

    private var loadingOverlay: View? = null

    fun showLoadingOverlay(isLoading: Boolean) {
        loadingOverlay?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Создаёт и добавляет оверлей загрузки поверх корневого View.
     * Корневой View должен быть ViewGroup (все наши layouts – это ViewGroup).
     */
    private fun setupLoadingOverlay() {
        val root = binding.root
        if (root !is ViewGroup) {
            // Если корень не ViewGroup (маловероятно), просто выходим
            return
        }

        loadingOverlay = FrameLayout(fragment.requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_overlay
                )
            )
            visibility = View.GONE

            val progressBar = ProgressBar(fragment.requireContext()).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                }
            }
            addView(progressBar)
        }
        root.addView(loadingOverlay)
    }

    fun onDestroyView() {
        _binding = null
        messageDisplayer = null
    }
}