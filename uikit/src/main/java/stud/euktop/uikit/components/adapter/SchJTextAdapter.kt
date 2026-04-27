package stud.euktop.uikit.components.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.databinding.ItemListTextBinding

class SchJTextAdapter<T>(
    private val listener: Listener<T>
) :
    ListAdapter<T, SchJTextAdapter.MyViewHolder>(object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(p0: T & Any, p1: T & Any) =
            listener.isEquals(p0, p1)

        override fun areContentsTheSame(p0: T & Any, p1: T & Any) = p0 == p1
    }) {
    interface Listener<T> {
        fun onClick(value: T)
        fun toText(value: T): String
        fun isEquals(value1: T, value2: T): Boolean
    }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ) = MyViewHolder(ItemListTextBinding.inflate(LayoutInflater.from(p0.context), p0, false))

    override fun onBindViewHolder(
        p0: MyViewHolder,
        p1: Int
    ) {
        val value = getItem(p1)
        p0.root.apply {
            text = listener.toText(value)
            setOnClickListener { listener.onClick(value) }
        }
    }

    class MyViewHolder(binding: ItemListTextBinding) : RecyclerView.ViewHolder(binding.root) {
        val root = binding.root
    }
}