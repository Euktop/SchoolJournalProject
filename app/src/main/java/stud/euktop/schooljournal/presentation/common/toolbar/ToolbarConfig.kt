package stud.euktop.schooljournal.presentation.common.toolbar

import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.annotation.StringRes

data class ToolbarConfig(
    @param:StringRes val titleRes: Int? = null,
    @param:MenuRes val menuRes: Int? = null,
    val onMenuItemClick: ((MenuItem) -> Unit)? = null
)

