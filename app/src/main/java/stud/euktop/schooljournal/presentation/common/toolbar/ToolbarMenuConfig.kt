package stud.euktop.schooljournal.presentation.common.toolbar

import android.view.MenuItem

interface ToolbarMenuConfig {
    val menuRes: Int?
    val onMenuItemClick: ((MenuItem) -> Unit)?
}