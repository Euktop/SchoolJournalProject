package stud.euktop.schooljournal.presentation.common.utils

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.components.recycler.SchJStatefulRecyclerView

fun <T> SchJStatefulRecyclerView.submitList(list: List<T>) {
    (adapter as? ListAdapter<T, *>)?.submitList(list) {
        update()
    }
}

fun <T> RecyclerView.submitList(list: List<T>) {
    this.adapter?.submitList(list = list)
}

fun <T> RecyclerView.Adapter<*>.submitList(list: List<T>) {
    (this as? ListAdapter<T, *>)?.submitList(list)
}