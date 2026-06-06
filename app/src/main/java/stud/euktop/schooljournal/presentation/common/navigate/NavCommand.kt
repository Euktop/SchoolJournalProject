package stud.euktop.schooljournal.presentation.common.navigate

import androidx.navigation.NavDirections
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager

/**
 * Иерархия команд навигации для использования в [NavigationManager].
 *
 * @author 12345 (номер участника)
 * @since 15-05-2026
 */
sealed class NavCommand {
    data class ToDestination(val destId: Int) : NavCommand()
    data class ToAction(val directions: NavDirections) : NavCommand()
    data class PopUpTo(val destinationId: Int, val inclusive: Boolean = false) : NavCommand()
    object Back : NavCommand()
}