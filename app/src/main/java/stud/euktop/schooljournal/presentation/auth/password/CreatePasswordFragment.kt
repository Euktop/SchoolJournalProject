package stud.euktop.schooljournal.presentation.auth.password

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentCreatePasswordBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindLoading
import stud.euktop.schooljournal.presentation.common.binding.setupPasswordForm
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack

/**
 * Экран создания пароля (финальный шаг регистрации).
 *
 * Назначение: позволяет пользователю задать пароль для новой учётной записи.
 * Пароль должен соответствовать требованиям безопасности:
 * - минимум 8 символов
 * - хотя бы одна заглавная буква
 * - хотя бы одна строчная буква
 * - хотя бы одна цифра
 * - хотя бы один специальный символ
 *
 * Роли: неавторизованные пользователи, завершающие регистрацию
 *
 * Функционал:
 * - Ввод нового пароля и его подтверждение
 * - Валидация пароля и совпадения
 * - Кнопка «Сохранить» активна только при валидных данных
 * - Регистрация через AuthCoordinator.register
 * - После успешной регистрации переход в главное меню
 *
 * @see CreatePasswordViewModel
 * @see stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
 */
@AndroidEntryPoint
class CreatePasswordFragment : BaseFragment<
        FragmentCreatePasswordBinding,
        CreatePasswordViewModel,
        CreatePasswordState,
        Unit>() {

    override val viewModel: CreatePasswordViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCreatePasswordBinding.inflate(inflater, container, false)

    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<CreatePasswordState>

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)
        binding.setupPasswordForm(this, focusTrack, viewModel, viewModel.updateState)

        binding.MatuleButtonSave.bindLoading(
            loadingDelegate,
            "register",
            R.string.saving
        )
        binding.MatuleButtonSave.setOnClickListener { viewModel.onSaveClick() }
    }

    override fun updateState(state: CreatePasswordState) {
        binding.MatuleButtonSave.isEnabled = state.isNextActive()
    }

    override fun updateEvent(event: Unit) {}
}