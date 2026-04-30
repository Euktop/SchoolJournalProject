package stud.euktop.schooljournal.presentation.common.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import stud.euktop.uikit.R
import stud.euktop.schooljournal.presentation.common.message.MessageEvent
import stud.euktop.schooljournal.presentation.common.message.impl.SnackBarMessages
import stud.euktop.schooljournal.presentation.common.message.contract.MessageDisplayer

/**
 * Базовый абстрактный фрагмент для всех экранов приложения.
 *
 * @param BINDING тип ViewBinding
 * @param VM тип ViewModel, наследующий BaseViewModel<STATE, EVENT>
 * @param STATE тип состояния (наследник BaseState<STATE>)
 * @param EVENT тип событий (sealed class/interface)
 *
 * Функционал:
 * - Автоматическая подписка на состояние (state) и события (event) ViewModel
 * - Отображение загрузочного оверлея при isLoading == true (через updateLoading)
 * - Обработка сообщений (snackbar) через MessageDisplayer
 * - Единая обработка жизненного цикла
 */
abstract class BaseFragment<BINDING : ViewBinding, VM : BaseViewModel<STATE, EVENT>, STATE : BaseState<STATE>, EVENT : Any> :
    Fragment() {

    protected lateinit var binding: BINDING
    protected lateinit var messages: MessageDisplayer

    /**
     * Получить ViewModel для этого фрагмента.
     */
    protected abstract val viewModel: VM

    /**
     * Инфлейтит ViewBinding.
     */
    protected abstract fun inflateBinding(i: LayoutInflater, c: ViewGroup?): BINDING

    /**
     * Настройка UI (клики, адаптеры, etc.) – вызывается после создания View.
     */
    protected abstract fun setupUI()

    /**
     * Обновление UI в соответствии с состоянием (когда isLoading == false).
     */
    protected abstract fun updateState(state: STATE = viewModel.state.value)

    /**
     * Обработка событий (навигация, диалоги и т.д.).
     */
    protected abstract fun updateEvent(event: EVENT)

    /**
     * Хук для кастомного отображения загрузки.
     * По умолчанию показывает/скрывает оверлей.
     * Наследники могут переопределить для своей логики (например, SwipeRefreshLayout).
     */
    protected open fun updateLoading(state: STATE) {
        showLoading(state.isLoading)
    }

    // ---------- Загрузочный оверлей ----------
    private var loadingOverlay: View? = null

    /**
     * Показывает или скрывает полупрозрачный оверлей с ProgressBar.
     */
    protected fun showLoading(isLoading: Boolean) {
        loadingOverlay?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // ---------- Жизненный цикл ----------
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
        setupLoadingOverlay()
        observeViewModel()
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

        loadingOverlay = FrameLayout(requireContext()).apply {
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

            val progressBar = ProgressBar(requireContext()).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                }
                // Можно кастомизировать цвет прогресса через инди, но оставим стандартный
            }
            addView(progressBar)
        }
        root.addView(loadingOverlay)
    }

    /**
     * Подписка на потоки state, event, messageEvent из ViewModel.
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateLoading(state)
                if (!state.isLoading) {
                    updateState(state)
                }
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

    /**
     * Обработка сообщений (Snackbar). Наследники могут переопределить.
     */
    protected open fun updateMessageEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.Message -> messages.message(event.param)
        }
    }
}