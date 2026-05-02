package stud.euktop.uikit.components.input.select.searchable

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.FragmentManager
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.SchJBaseSelect

class SchJSearchableSelect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SchJBaseSelect(context, attrs, defStyleAttr) {

    private var fragmentManager: FragmentManager? = null
    private var config: SchJSearchableSelectDialogConfig<*>? = null
    private var currentDialog: SchJSearchableSelectDialog<*>? = null
    override fun onClearText() {
        config?.items?.onClick(null, true)
    }

    override fun showSelectionDialog() {
        val fm = fragmentManager ?: return
        val config = config?.copy(title = state.title ?: "") ?: return
        this.config = config
        val dialog = SchJSearchableSelectDialog(config)
        currentDialog = dialog
        dialog.show(fm, "searchable_select_${hashCode()}")
    }

    inner class RegisterList<T : Any>(
        private val items: ListSafe<T>,
        private val showFilterDialog: (() -> Unit)? = null
    ) {
        fun register(fragmentManager: FragmentManager) {
            this@SchJSearchableSelect.fragmentManager = fragmentManager
            updateItems(items)
        }

        fun updateItems(newItems: List<T>) {
            updateItems(this.items.copy(values = newItems))
        }

        fun updateItems(newItems: ListSafe<T>) {
            this@SchJSearchableSelect.config = SchJSearchableSelectDialogConfig(
                title = "",
                items = newItems.copy { t, bool ->
                    newItems.onClick(t, bool)
                    state = state.copy(selectText = newItems.toText(t))
                },
                showFilterDialog = showFilterDialog
            )
            (currentDialog as? SchJSearchableSelectDialog<T>)?.updateConfig(newItems.values)
        }
    }
}