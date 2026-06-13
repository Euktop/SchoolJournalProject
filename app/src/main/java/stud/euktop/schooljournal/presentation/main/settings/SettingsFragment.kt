package stud.euktop.schooljournal.presentation.main.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentSettingsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.uikit.R
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

@AndroidEntryPoint
class SettingsFragment :
    BaseFragment<FragmentSettingsBinding, SettingsViewModel, SettingsState, SettingsEvent>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override val viewModel: SettingsViewModel by viewModels()

    override fun setupUI() {
        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDarkTheme(isChecked)
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleNotifications(isChecked)
        }

        binding.btnClearCache.setOnClickListener {
            viewModel.clearCache()
        }

        binding.btnAboutApp.setOnClickListener {
            viewModel.openAboutApp()
        }

        binding.rowLanguage.setOnClickListener {
            showLanguageDialog()
        }
    }

    override fun updateState(state: SettingsState) {
        try {
            logger?.d(this.toSimpleTag(), "updateState", "isDarkTheme=${state.isDarkTheme}, isNotifications=${state.isNotificationsEnabled}, language=${state.currentLanguage}")
        } catch (_: Throwable) {
        }
        binding.switchDarkTheme.isChecked = state.isDarkTheme
        binding.switchNotifications.isChecked = state.isNotificationsEnabled
        binding.tvLanguageValue.text = state.currentLanguage
    }

    override fun updateEvent(event: SettingsEvent) {
        try {
            logger?.d(this.toSimpleTag(), "updateEvent", "event=${event::class.java.simpleName}")
        } catch (_: Throwable) {
        }
        when (event) {
            is SettingsEvent.ShowAboutDialog -> showAboutDialog(event.info)
        }
    }

    private fun showAboutDialog(info: String) {
        try {
            logger?.d(this.toSimpleTag(), "showAboutDialog", "info_len=${info.length}")
        } catch (_: Throwable) {
        }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.about_app)
            .setMessage(info)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun showLanguageDialog() {
        try {
            logger?.d(this.toSimpleTag(), "showLanguageDialog")
        } catch (_: Throwable) {
        }
        val languages = arrayOf(
            getString(R.string.language_russian),
            getString(R.string.language_english)
        )
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_language)
            .setItems(languages) { _, which ->
                val selected = when (which) {
                    0 -> getString(R.string.language_russian)
                    1 -> getString(R.string.language_english)
                    else -> return@setItems
                }
                viewModel.changeLanguage(selected)
            }
            .show()
    }
}