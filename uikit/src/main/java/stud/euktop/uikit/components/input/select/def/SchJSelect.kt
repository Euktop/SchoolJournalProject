package stud.euktop.uikit.components.input.select.def

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.components.input.select.SchJBaseSelect
import stud.euktop.uikit.components.input.select.content.SelectableAdapter

class SchJSelect @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SchJBaseSelect(context, attrs, defStyleAttr) {

    private var fragmentManager: FragmentManager? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var select: SelectableAdapter<*>? = null
    private var isShowing = false
    private var dialog: SchJSelectSheet? = null

    override fun onClearText() {
        select?.onItemSelected(null)
        dialog?.dismiss()
    }

    fun <T> attach(
        adapter: RecyclerView.Adapter<*>,
        select: SelectableAdapter<T>,
        fragmentManager: FragmentManager
    ) {
        val old = select.onItemSelected
        select.onItemSelected = { it ->
            old(it)
            dialog?.dismiss()
            state = state.copy(selectText = select.toText(it))
        }
        this.select = select
        this.adapter = adapter
        this.fragmentManager = fragmentManager
        this.dialog?.dismiss()
    }

    override fun showSelectionDialog() {
        if (isShowing) return
        val fm = fragmentManager ?: return
        val adapter = adapter ?: return
        dialog = SchJSelectSheet(adapter) {
            isShowing = false
            dialog = null
        }
        isShowing = true
        dialog?.show(fm, this::class.simpleName)
    }
}