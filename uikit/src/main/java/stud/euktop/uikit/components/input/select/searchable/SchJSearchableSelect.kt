package stud.euktop.uikit.components.input.select.searchable

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.components.input.select.SchJBaseSelect
import stud.euktop.uikit.components.input.select.content.SelectableAdapter

class SchJSearchableSelect @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SchJBaseSelect(context, attrs, defStyleAttr) {

    private var fragmentManager: FragmentManager? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var selectable: SelectableAdapter<*>? = null
    private var dialogTitle: String = ""
    private var onFilterClick: (() -> Unit)? = null
    private var isShowing = false
    private var dialog: SchJSearchableSelectDialog? = null

    override fun onClearText() {
        selectable?.onItemSelected(null)
        dialog?.dismiss()
    }

    fun <T> attach(
        adapter: RecyclerView.Adapter<*>,
        selectable: SelectableAdapter<T>,
        fragmentManager: FragmentManager,
        title: String = "",
        onFilterClick: (() -> Unit)? = null
    ) {
        val old = selectable.onItemSelected
        selectable.onItemSelected = { item ->
            old(item)
            dialog?.dismiss()
            state = state.copy(selectText = selectable.toText(item))
        }
        this.adapter = adapter
        this.selectable = selectable
        this.fragmentManager = fragmentManager
        this.dialogTitle = title
        this.onFilterClick = onFilterClick
        this.dialog?.dismiss()
    }

    override fun showSelectionDialog() {
        if (isShowing) return
        val fm = fragmentManager ?: return
        val currentAdapter = adapter ?: return
        dialog = SchJSearchableSelectDialog(
            adapter = currentAdapter,
            title = dialogTitle,
            onFilterClick = onFilterClick,
            onDismiss = { isShowing = false; dialog = null }
        )
        isShowing = true
        dialog?.show(fm, this::class.simpleName)
    }
}