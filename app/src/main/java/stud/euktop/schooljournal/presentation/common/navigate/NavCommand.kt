package stud.euktop.schooljournal.presentation.common.navigate

import android.os.Bundle
import androidx.navigation.NavDirections

sealed class NavCommand {
    data class ToDestination(
        val destId: Int,
        val args: Bundle? = null,
        val targetKey: String? = null,
        val popUpTo: Int? = null,
        val inclusive: Boolean = false
    ) : NavCommand()

    data class ToAction(val directions: NavDirections) : NavCommand()
    object Back : NavCommand()
}