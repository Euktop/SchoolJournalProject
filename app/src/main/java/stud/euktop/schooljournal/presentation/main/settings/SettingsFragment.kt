package stud.euktop.schooljournal.presentation.main.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentSettingsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<
    FragmentSettingsBinding,
    SettingsViewModel,
    SettingsState,
    Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override val viewModel: SettingsViewModel by viewModels()

    @Inject
    internal lateinit var navigationManager: NavigationManager

    @Inject
    internal lateinit var router: RouterProfile

    override fun setupUI() {
        // Кнопка "Назад"
        binding.btnBack.setOnClickListener {
            navigationManager.navigate(stud.euktop.schooljournal.presentation.common.navigate.NavCommand.Back)
        }

        // Переключатели
        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDarkTheme(isChecked)
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleNotifications(isChecked)
        }

        // Кнопки действий
        binding.btnClearCache.setOnClickListener {
            viewModel.clearCache()
        }

        binding.btnAboutApp.setOnClickListener {
            viewModel.openAboutApp()
        }

        // Строка языка (пока заглушка)
        binding.rowLanguage.setOnClickListener {
            // TODO: Открыть выбор языка
        }
    }

    override fun updateState(state: SettingsState) {
        binding.switchDarkTheme.isChecked = state.isDarkTheme
        binding.switchNotifications.isChecked = state.isNotificationsEnabled
        binding.tvLanguageValue.text = state.currentLanguage
    }

    override fun updateEvent(event: Unit) {}
}