package stud.euktop.domain.model.settings

data class Settings(
    val isDarkTheme: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val currentLanguage: String = "Русский"
)