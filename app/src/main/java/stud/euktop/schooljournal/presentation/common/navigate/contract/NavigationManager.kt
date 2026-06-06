package stud.euktop.schooljournal.presentation.common.navigate.contract

import androidx.navigation.NavController
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand

/**
 * Управляет навигацией между экранами приложения.
 * Позволяет регистрировать [NavController] по ключам, выполнять команды [NavCommand]
 * и автоматически обрабатывать отложенные переходы.
 *
 * @author 12345 (номер участника)
 * @since 15-05-2026
 */
@Deprecated("Используй роутер")
interface NavigationManager {

    /**
     * Выполняет одну или несколько навигационных команд последовательно.
     * @param cmds команды навигации (переход назад, к экрану, к действию)
     */
    fun navigate(vararg cmds: NavCommand)

    /**
     * Привязывает главный NavController (обычно для корневого Activity).
     * Все команды без явного targetKey будут направляться на этот контроллер.
     * @param navController главный контроллер навигации
     */
    fun bindMain(navController: NavController)

    /**
     * Регистрирует NavController для определённого ключа (например, идентификатор вложенного графа).
     * @param key уникальный ключ, по которому будет найден контроллер
     * @param navController контроллер для этого ключа
     */
    fun register(key: String, navController: NavController)

    /**
     * Удаляет ранее зарегистрированный контроллер.
     * @param key ключ, по которому контроллер был зарегистрирован
     */
    fun unregister(key: String)
}