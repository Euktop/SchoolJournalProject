package stud.euktop.schooljournal.presentation.common.navigate

/**Правила очистки стека*/
enum class StackAction {
    /**Добавить текущий фрагмент в back stack*/
    KEEP,

    /**Уничтожить текущий фрагмент*/
    REPLACE,

    /**Очистить весь стек*/
    CLEAR_ALL,

    /**Очищает текущую группу*/
    CLEAR_GROUP
}