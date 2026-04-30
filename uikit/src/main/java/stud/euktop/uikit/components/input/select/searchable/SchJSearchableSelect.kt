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
    private var config: SchJSearchableSelectDialogConfig<*, *> =
        SchJSearchableSelectDialogConfig<Any, Any>()
    private var currentDialog: SchJSearchableSelectDialog<*, *>? = null

    override fun showSelectionDialog() {
        val fm = fragmentManager ?: return
        config = config.copy(title = state.title ?: "")
        val dialog = SchJSearchableSelectDialog(config)
        currentDialog = dialog
        dialog.show(fm, "searchable_select_${hashCode()}")
    }

    fun <T : Any> RegisterList(
        items: ListSafe<T>,
        onSearchQueryChanged: (String) -> Unit = {},
    ) = RegisterList(items, ListSafe(), onSearchQueryChanged)

    inner class RegisterList<T : Any, C : Any>(
        private val items: ListSafe<T>,
        private val categories: ListSafe<C> = ListSafe(),
        private val onSearchQueryChanged: (String) -> Unit = {},
    ) {
        fun register(
            fragmentManager: FragmentManager,
            items: ListSafe<T> = this.items,
            categories: ListSafe<C> = this.categories
        ) {
            this@SchJSearchableSelect.fragmentManager = fragmentManager
            updateItems(items, categories)
        }

        fun updateItems(
            newItems: List<T> = this.items.values,
            categories: List<C> = this.categories.values
        ) =
            updateItems(
                this.items.copy(values = newItems),
                this.categories.copy(values = categories)
            )

        fun updateItems(
            newItems: ListSafe<T> = this.items,
            categories: ListSafe<C> = this.categories
        ) {
            this@SchJSearchableSelect.config = SchJSearchableSelectDialogConfig(
                title = "",
                items = newItems,
                categories = categories,
                onSearchQueryChanged = onSearchQueryChanged
            )
            (currentDialog as? SchJSearchableSelectDialog<T, C>)?.apply {
                this.updateItems(newItems.values)
                this.updateCategories(categories.values)
            }
        }
    }
}