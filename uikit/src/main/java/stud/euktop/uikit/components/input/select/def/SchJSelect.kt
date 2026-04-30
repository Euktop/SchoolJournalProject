// uikit/src/main/java/stud/euktop/uikit/components/input/select/SchJSelect.kt
package stud.euktop.uikit.components.input.select.def

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.FragmentManager
import stud.euktop.uikit.components.adapter.SchJTextAdapter
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.SchJBaseSelect

class SchJSelect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SchJBaseSelect(context, attrs, defStyleAttr) {

    private var fragmentManager: FragmentManager? = null
    private var allItems: List<Any> = emptyList()
    private var toText: (Any) -> String = { it.toString() }
    private var onSelectedListener: ((Any) -> Unit)? = null

    override fun showSelectionDialog() {
        val fm = fragmentManager ?: return
        val adapter = createAdapter()
        val dialog = SchJSelectSheet(adapter)
        dialog.show(fm, "select_sheet_${hashCode()}")
    }

    private fun createAdapter(): SchJTextAdapter<Any> {
        return SchJTextAdapter(object : SchJTextAdapter.Listener<Any> {
            override fun onClick(value: Any) {
                onSelectedListener?.invoke(value)
                setSelectedText(toText(value))
            }

            override fun toText(value: Any): String = this@SchJSelect.toText(value)
            override fun isEquals(value1: Any, value2: Any): Boolean = value1 == value2
        }).apply { submitList(allItems) }
    }

    inner class RegisterList<T : Any>(
        val items: ListSafe<T>
    ) {
        fun register(fragmentManager: FragmentManager, items: ListSafe<T> = this.items) {
            this@SchJSelect.fragmentManager = fragmentManager
            updateItems(items)
        }

        fun updateItems(items: List<T> = this.items.values) {
            updateItems(this.items.copy(values = items))
        }

        fun updateItems(items: ListSafe<T> = this.items) {
            this@SchJSelect.allItems = items.values
            this@SchJSelect.toText = { obj -> items.toText(obj as T) }
            this@SchJSelect.onSelectedListener = { obj -> items.onClick(obj as T, true) }
        }
    }
}