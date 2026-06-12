package stud.euktop.schooljournal.presentation.common.toolbar

import android.view.MenuItem
import androidx.annotation.MenuRes

data class ToolbarStringConfig(
    val title: String? = null,
    @param:MenuRes override val menuRes: Int? = null,
    override val onMenuItemClick: ((MenuItem) -> Unit)? = null
) : ToolbarMenuConfig

