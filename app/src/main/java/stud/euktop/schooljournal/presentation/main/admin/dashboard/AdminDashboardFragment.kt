package stud.euktop.schooljournal.presentation.main.admin.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.loger.logger
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminDashboardBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class AdminDashboardFragment :
    BaseFragment<FragmentAdminDashboardBinding, AdminDashboardViewModel, AdminDashboardState, Unit>(),
    ToolbarConfigProvider {

    override val viewModel: AdminDashboardViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminDashboardBinding.inflate(inflater, container, false)

    override fun setupUI() {
        // Кнопки Generate Report, Invite, Maintenance удалены из UI (или закомментированы)
        // Если они нужны в будущем, их обработчики пока можно убрать.
        // TODO: Кнопка "Недавние входы" (Recent Logins chart) использует мок-данные
        // Реальные данные из backend-а интегрированы в ViewModel через AdminDashboardRepository
    }

     override fun updateState(state: AdminDashboardState) {
         logger?.d(this::class.java.simpleName, "updateState", "adminName: ${state.adminName}, schools: ${state.schoolsCount}, users: ${state.activeUsersCount}, students: ${state.totalStudents}, teachers: ${state.totalTeachers}")
         binding.tvWelcomeTitle.text =
             requireContext().getString(
                 stud.euktop.uikit.R.string.welcome_admin_format,
                 state.adminName
             )
         binding.tvSchoolsCount.text = state.schoolsCount.toString()
         binding.tvActiveUsersCount.text = state.activeUsersCount.toString()
         binding.tvTotalStudents.text = state.totalStudents.toString()
         binding.tvTotalTeachers.text = state.totalTeachers.toString()
         binding.tvHealthPercent.text =
             getString(stud.euktop.uikit.R.string.percent_format, state.healthPercent)
     }

    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig(): ToolbarConfig = ToolbarConfig(
        titleRes = stud.euktop.uikit.R.string.dashboard,
        menuRes = R.menu.menu_home,
        onMenuItemClick = { menuItem ->
            when (menuItem.itemId) {
                R.id.action_profile -> viewModel.onProfileClick()
            }
        })
}