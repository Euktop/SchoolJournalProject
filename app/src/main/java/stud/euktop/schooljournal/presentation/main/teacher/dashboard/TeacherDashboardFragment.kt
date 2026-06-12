package stud.euktop.schooljournal.presentation.main.teacher.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentTeacherDashboardBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

import stud.euktop.schooljournal.R as RApp
import stud.euktop.uikit.R as RUi

@AndroidEntryPoint
class TeacherDashboardFragment : BaseFragment<
        FragmentTeacherDashboardBinding,
        TeacherDashboardViewModel,
        TeacherDashboardState,
        Unit>(), ToolbarConfigProvider {

    override val viewModel: TeacherDashboardViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherDashboardBinding.inflate(inflater, container, false)

    override fun setupUI() {
        // TODO: Add UI setup
    }

    override fun updateState(state: TeacherDashboardState) {
        // TODO: Update UI based on state
    }

    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = RUi.string.app_name,
        menuRes = RApp.menu.menu_home
    )
}

