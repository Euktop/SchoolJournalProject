package stud.euktop.uikit.components.filter

import android.content.Context
import android.util.AttributeSet
import stud.euktop.uikit.R

class SchJFilterToolBar @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle
) : androidx.appcompat.widget.Toolbar(context, attr, defStyleAttr) {
    init {
        setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> {
                    showFilterDialog?.invoke()
                    true
                }

                else -> false
            }
        }
    }

    var showFilterDialog: (() -> Unit)? = null
}