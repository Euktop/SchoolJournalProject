package stud.euktop.schooljournal.presentation.main.student.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentStudentScheduleBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.filter.lesson.LessonFilterDialog
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import stud.euktop.schooljournal.presentation.common.utils.submitList

@AndroidEntryPoint
class StudentScheduleFragment : BaseFragment<
        FragmentStudentScheduleBinding,
        StudentScheduleViewModel,
        StudentScheduleState,
        Unit>(), ToolbarConfigProvider {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentScheduleBinding.inflate(inflater, container, false)

    override val viewModel: StudentScheduleViewModel by viewModels()

    private lateinit var adapter: StudentScheduleAdapter

    override fun setupUI() {
        adapter = StudentScheduleAdapter {}
        binding.rvSchedule.adapter = adapter
    }

    override fun updateState(state: StudentScheduleState) {
        binding.rvSchedule.submitList(state.schedule)
    }

    private fun showFilter() {
        if (parentFragmentManager.findFragmentByTag("lesson_filter") != null) return
        LessonFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = { filter -> viewModel.applyFilter(filter) },
            onError = viewModel.onError
        ).show(childFragmentManager, "lesson_filter")
    }

    override fun updateEvent(event: Unit) {}
    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = stud.euktop.uikit.R.string.schedule,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = {
            when (it.itemId) {
                R.id.action_filter -> {
                    showFilter()
                }
            }
        }
    )
}