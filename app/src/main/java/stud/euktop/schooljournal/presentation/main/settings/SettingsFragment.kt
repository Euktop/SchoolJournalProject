package stud.euktop.schooljournal.presentation.main.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentSettingsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment

@AndroidEntryPoint
class SettingsFragment : BaseFragment<
        FragmentSettingsBinding,
        SettingsViewModel,
        SettingsState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override val viewModel: SettingsViewModel by viewModels()

    override fun setupUI() {
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