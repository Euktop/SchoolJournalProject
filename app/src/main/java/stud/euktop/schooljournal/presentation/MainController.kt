package stud.euktop.schooljournal.presentation

import androidx.annotation.IdRes

/**
 * Интерфейс для переключения табов BottomNavigation у родительского хоста (StudentHome/TeacherHome).
 * Фрагменты, находящиеся внутри хоста, будут приводить parentFragment к этому интерфейсу
 * и вызывать switchToTab(id) вместо глобальной навигации через Router.
 */
interface MainController {
    fun switchToTab(@IdRes menuItemId: Int)
}

