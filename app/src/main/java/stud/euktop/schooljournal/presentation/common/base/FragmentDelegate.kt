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

/**
 * Делегат для управления фрагментом с MVVM (ViewBinding + ViewModel + State + Event).
 *
 * @param fragment Фрагмент, с которым связан делегат.
 * @param viewModel ViewModel, наследующая BaseViewModel.
 * @param inflateBinding Функция инфлейта ViewBinding.
 * @param setupUI Вызывается после создания View – для настройки UI (клики, адаптеры).
 * @param updateState Вызывается при новом состоянии (когда isLoading == false).
 * @param updateEvent Вызывается при новом событии.
 * @param onLoadingChange Опционально – кастомное отображение загрузки (по умолчанию оверлей).
 */
class FragmentDelegate<BINDING : ViewBinding, VM : BaseViewModel<STATE, EVENT>, STATE : BaseState<STATE>, EVENT : Any>(
    private val fragment: Fragment,
    private val viewModel: VM,
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> BINDING,
    private val setupUI: (BINDING) -> Unit,
    private val updateState: (BINDING, STATE) -> Unit,
    private val updateEvent: (BINDING, EVENT) -> Unit,
    private val onLoadingChange: ((Boolean) -> Unit)? = null
) {

    lateinit var binding: BINDING
        private set

    private var messages: SnackBarMessages? = null
    private var loadingOverlay: View? = null

    /**
     * Вызывается в [Fragment.onCreateView].
     * @return корневой View
     */
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateBinding(inflater, container, false)
        messages = SnackBarMessages(binding.root)
        setupLoadingOverlay()
        return binding.root
    }

    /**
     * Вызывается в [Fragment.onViewCreated].
     */
    fun onViewCreated() {
        setupUI(binding)
        observeViewModel()
    }

    /**
     * Вызывается в [Fragment.onDestroyView] – очищает оверлей.
     */
    fun onDestroyView() {
        loadingOverlay?.let { (binding.root as? ViewGroup)?.removeView(it) }
        loadingOverlay = null
        messages = null
    }

    private fun setupLoadingOverlay() {
        val root = binding.root
        if (root !is ViewGroup) return
        loadingOverlay = android.widget.FrameLayout(fragment.requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(
                androidx.core.content.ContextCompat.getColor(
                    context,
                    stud.euktop.uikit.R.color.color_overlay
                )
            )
            visibility = View.GONE
            addView(android.widget.ProgressBar(context).apply {
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { gravity = android.view.Gravity.CENTER }
            })
        }
        root.addView(loadingOverlay)
    }

    private fun showLoading(show: Boolean) {
        onLoadingChange?.invoke(show) ?: run {
            loadingOverlay?.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    private fun observeViewModel() {
        fragment.lifecycleScope.launch {
            viewModel.state.collect { state ->
                showLoading(state.isLoading)
                if (!state.isLoading) {
                    updateState(binding, state)
                }
            }
        }
        fragment.lifecycleScope.launch {
            viewModel.event.collect { event ->
                updateEvent(binding, event)
            }
        }
        fragment.lifecycleScope.launch {
            viewModel.messageEvent.collect { event ->
                when (event) {
                    is MessageEvent.Message -> messages?.message(event.param)
                }
            }
        }
    }
}