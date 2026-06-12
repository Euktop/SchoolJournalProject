package stud.euktop.uikit.components.categories

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.components.button.SchJButtonState
import stud.euktop.uikit.components.filter.Category
import stud.euktop.uikit.databinding.ItemCategoryBinding
import stud.euktop.uikit.util.PxDpSp

class SchJButtonCategories @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attr, defStyleAttr) {
    init {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        addItemDecoration(Decoration)
        clipChildren = false
        clipToPadding = false
    }

    class Adapter<V : Category<T>, T>(
        val toText: (T) -> String = { it.toString() },
        val onClick: (T, Boolean) -> Unit = { _, _ -> },
    ) : ListAdapter<V, Adapter.VH>(Back<V, T>()) {
        override fun onCreateViewHolder(
            p0: ViewGroup,
            p1: Int
        ) = VH(ItemCategoryBinding.inflate(LayoutInflater.from(p0.context), p0, false))

        override fun onBindViewHolder(
            p0: VH,
            p1: Int
        ) {
            p0.binding.root.apply {
                val item = getItem(p1)
                text = toText(item.value)
                state = state.copy(buttonClass = state.buttonClass.updateSelect(item.isSelected))
                setOnClickListener {
                    val clas =
                        if (state.buttonClass.isSelect()) SchJButtonState.ButtonClass.UNSELECT
                        else SchJButtonState.ButtonClass.SELECT
                    state = state.copy(buttonClass = clas)
                    onClick(item.value, clas.isSelect())
                }
            }
        }


        class VH(val binding: ItemCategoryBinding) : ViewHolder(binding.root)
        class Back<V : Category<T>, T> : DiffUtil.ItemCallback<V>() {
            override fun areItemsTheSame(p0: V, p1: V): Boolean {
                return p0.value == p1.value
            }

            override fun areContentsTheSame(p0: V, p1: V): Boolean {
                return p0 == p1
            }

        }
    }

    object Decoration : ItemDecoration() {
        val spacing = PxDpSp.dp(8f).px.toInt()
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            val itemCount = state.itemCount
            if (position > 0) outRect.left = spacing
            if (position < itemCount - 1) outRect.right = spacing
        }
    }

    fun <V> initAdapter(
        toText: (V) -> String = { it.toString() }, onClick: (V, Boolean) -> Unit = { _, _ -> },
    ) {
        adapter = Adapter<Category<V>, V>(toText = toText, onClick = onClick)
    }

    fun <T> updateList(list: List<Pair<T, Boolean>>) {
        (adapter as? ListAdapter<Category<T>, *>)?.submitList(list.map {
            Category(
                it.first,
                it.second
            )
        })
    }

    fun <T> updateListCategory(list: List<Category<T>>) {
        (adapter as? ListAdapter<Category<T>, *>)?.submitList(list.map {
            it
        })
    }
}