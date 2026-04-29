package stud.euktop.schooljournal.presentation.auth.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.Nav1Directions
import stud.euktop.schooljournal.databinding.ActivityLoginBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import stud.euktop.schooljournal.presentation.common.utils.setup
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator

/**
 * Экран авторизации (вход по email и паролю).
 *
 * Назначение: позволяет пользователю войти в приложение, указав email и пароль.
 * После успешной авторизации перенаправляет в главное меню (MainMenuFragment).
 *
 * Роли: все неавторизованные пользователи (ученик, учитель, родитель, администратор)
 *
 * Функционал:
 * - Ввод email и пароля с валидацией (EmailValidator, PasswordValidator)
 * - Кнопка входа (активна только при валидных данных)
 * - Отображение индикации загрузки во время запроса
 * - Обработка ошибок авторизации (snackbar с сообщением)
 * - Навигация к экрану регистрации (ProfileFragment)
 *
 * @see LoginViewModel
 * @see AuthCoordinator
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment<ActivityLoginBinding, LoginViewModel, LoginState, LoginEvent>() {
    override fun inflateBinding(
        i: LayoutInflater,
        c: ViewGroup?
    ) = ActivityLoginBinding.inflate(i, c, false)

    override val viewModel: LoginViewModel by viewModels()
    val focusTrack = FocusTrack()

    override fun setupUI() {
        binding.apply {
            MatuleInputEmail.setup(focusTrack) { viewModel.emailSet(it) }
            MatuleInputPassword.setup(focusTrack) { viewModel.passwordSet(it) }
            matuleButtonAuth.isEnabled = false
            matuleButtonAuth.setOnClickListener {
                viewModel.onLoginClick()
            }
        }
    }

    @javax.inject.Inject
    internal lateinit var navigationManager: NavigationManager
    override fun updateState(state: LoginState) {
        binding.MatuleInputEmail.check(focusTrack, state.emailValidator)
        binding.MatuleInputPassword.check(focusTrack, state.passwordValidator)
        binding.matuleButtonAuth.isEnabled = state.isButtonActive()
    }

    override fun updateEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.NavigateToMain -> navigationManager.navigate(
                NavCommand.ToAction(Nav1Directions.actionGlobalNavMainMain())
            )
        }
    }
}