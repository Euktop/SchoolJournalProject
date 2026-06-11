package stud.euktop.schooljournal.presentation.main.student.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentStudentDashboardBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class StudentDashboardFragment : BaseFragment<
        FragmentStudentDashboardBinding,
        StudentDashboardViewModel,
        StudentDashboardState,
        Unit>(), ToolbarConfigProvider {

    override val viewModel: StudentDashboardViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentDashboardBinding.inflate(inflater, container, false)

    override fun setupUI() {
        // TODO: Add UI setup
    }

    override fun updateState(state: StudentDashboardState) {
        // TODO: Update UI based on state
    }

    override fun updateEvent(event: Unit) {}
    override fun getToolbarConfig() = ToolbarConfig()
}

