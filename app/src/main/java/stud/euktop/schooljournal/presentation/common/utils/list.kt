package stud.euktop.schooljournal.presentation.common.utils

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.components.recycler.SchJStatefulRecyclerView
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

fun <T> SchJStatefulRecyclerView.submitList(list: List<T>) {
    logger?.d(this.toSimpleTag(), "submitList", "items count: ${list.size}")
    (adapter as? ListAdapter<T, *>)?.submitList(list) {
        update()
    }
}

fun <T> RecyclerView.submitList(list: List<T>) {
    logger?.d("RecyclerView", "submitList", "items count: ${list.size}")
    this.adapter?.submitList(list = list)
}

fun <T> RecyclerView.Adapter<*>.submitList(list: List<T>) {
    logger?.d("Adapter", "submitList", "items count: ${list.size}")
    (this as? ListAdapter<T, *>)?.submitList(list)
}