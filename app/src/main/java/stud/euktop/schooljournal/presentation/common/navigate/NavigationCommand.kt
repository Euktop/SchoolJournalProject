package stud.euktop.schooljournal.presentation.common.navigate

sealed class NavigationCommand {
    // Глобальные (используют rootNavController)
    object ToAuth : NavigationCommand()
    object ToMenu : NavigationCommand()
    object ToRegistration : NavigationCommand()

    // Внутри текущей вкладки (используют currentTabNavController)
    data class ToOrder(val fromMain: Boolean) : NavigationCommand()
    data class ToProfile(val userId: String) : NavigationCommand()
    object ToCreateProject : NavigationCommand()

    // Явное указание вкладки (если нужно переключить вкладку и там что-то открыть)
    data class SwitchTabAndNavigate(
        val tabId: String,
        val nestedCommand: NavigationCommand
    ) : NavigationCommand()
}