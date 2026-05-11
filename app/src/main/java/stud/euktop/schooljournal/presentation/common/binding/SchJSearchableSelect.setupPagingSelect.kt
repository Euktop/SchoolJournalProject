package stud.euktop.schooljournal.presentation.common.binding

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.presentation.common.adapter.PagingSelectAdapter
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect

fun <T : Any> SchJSearchableSelect.setupPagingSelect(
    fragmentManager: FragmentManager,
    title: String,
    pagingDataFlow: Flow<PagingData<T>>,
    toText: (T?) -> String,
    onSelected: (T?) -> Unit,
    onFilterClick: (() -> Unit)? = null,
    lifecycleOwner: LifecycleOwner
) {
    val adapter = PagingSelectAdapter(toText, onSelected)
    attach(adapter, adapter, fragmentManager, title, onFilterClick)
    lifecycleOwner.lifecycleScope.launch {
        pagingDataFlow.collectLatest { pagingData ->
            adapter.submitData(pagingData)
        }
    }
}