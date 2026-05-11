package stud.euktop.schooljournal.presentation.common.binding

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.adapter.PagingSelectAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.utils.observeState
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect

fun <T : Any> SchJSearchableSelect.setupPagingSelect(
    fragmentManager: FragmentManager,
    title: String,
    pagingDataFlow: Flow<PagingData<T>>,
    toText: (T?) -> String,
    onSelected: (T?) -> Unit,
    lifecycleOwner: LifecycleOwner,
    onFilterClick: (() -> Unit)? = null
) {
    val adapter = PagingSelectAdapter(toText, onSelected)
    attach(adapter, adapter, fragmentManager, title, onFilterClick)
    lifecycleOwner.lifecycleScope.launch {
        pagingDataFlow.collectLatest { pagingData ->
            adapter.submitData(pagingData)
        }
    }
}

/**
 * Привязывает SchJSearchableSelect к потоку фильтров и PagingData.
 * @param filterFlow - поток фильтров (например, SchoolFilter, UserFilter)
 * @param pagingDataProvider - функция, которая по фильтру возвращает Flow<PagingData<T>>
 * @param toText - преобразование элемента в текст для отображения
 * @param onSelected - колбэк выбора элемента
 * @param title - заголовок диалога
 * @param onFilterClick - действие при нажатии кнопки фильтра (опционально)
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Any, F> SchJSearchableSelect.bindPagingSelect(
    fragmentManager: FragmentManager,
    lifecycleOwner: LifecycleOwner,
    filterFlow: Flow<F>,
    pagingDataProvider: suspend (F) -> Flow<PagingData<T>>,
    toText: (T?) -> String,
    onSelected: (T?) -> Unit,
    title: String,
    onFilterClick: (() -> Unit)? = null
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            filterFlow.flatMapLatest { filter ->
                pagingDataProvider(filter)
            }.collectLatest { pagingData ->
                val adapter = PagingSelectAdapter(
                    toText = toText, onItemSelected = onSelected
                )
                attach(adapter, adapter, fragmentManager, title, onFilterClick)
                adapter.submitData(pagingData)
            }
        }
    }
}

/**
 * Привязывает поисковый селект (SchJSearchableSelect) к PagingData потоку.
 *
 * @param select компонент выбора
 * @param viewModel ViewModel, предоставляющая состояние и потоки данных
 * @param title заголовок диалога выбора
 * @param filterFlow поток фильтров (при изменении перезагружаются данные)
 * @param getPagingDataFlow функция, возвращающая Flow<PagingData<T>> по заданному фильтру
 * @param getSelectedValue функция получения выбранного значения из состояния ViewModel
 * @param toText преобразование выбранного элемента в текст для отображения
 * @param onSelected колбэк выбора элемента
 * @param onFilterClick колбэк нажатия на кнопку фильтра (открывает диалог фильтрации)
 * @param fragmentManager FragmentManager для отображения диалога выбора
 * @param lifecycleOwner LifecycleOwner (обычно viewLifecycleOwner) для отписки
 */
fun <STATE : BaseState<STATE>, T : Any, FILTER : Any> bindPagingSelect(
    select: SchJSearchableSelect,
    viewModel: BaseViewModel<STATE, *>,
    title: String,
    filterFlow: MutableStateFlow<FILTER>,
    getPagingDataFlow: suspend (FILTER) -> Flow<PagingData<T>>,
    getSelectedValue: (STATE) -> T?,
    toText: (T?) -> String,
    onSelected: (T?) -> Unit,
    onFilterClick: () -> Unit,
    fragmentManager: FragmentManager,
    lifecycleOwner: LifecycleOwner
) {
    // Подписка на изменения фильтра и загрузка PagingData
    lifecycleOwner.lifecycleScope.launch {
        filterFlow.flatMapLatest { filter ->
            getPagingDataFlow(filter)
        }.collectLatest { pagingData ->
            val adapter = PagingSelectAdapter(
                toText = toText, onItemSelected = onSelected
            )
            select.attach(adapter, adapter, fragmentManager, title, onFilterClick)
            adapter.submitData(pagingData)
        }
    }
    lifecycleOwner.observeState(viewModel.state, lifecycleOwner) { state ->
        val value = getSelectedValue(state)
        select.state = select.state.copy(selectText = toText(value))
    }
}