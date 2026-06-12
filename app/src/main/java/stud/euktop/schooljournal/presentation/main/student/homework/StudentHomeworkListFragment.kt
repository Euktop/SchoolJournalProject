package stud.euktop.schooljournal.presentation.main.student.homework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentStudentHomeworkListBinding
import stud.euktop.schooljournal.presentation.common.adapter.HomeworkAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.filter.studenthomework.StudentHomeworkFilterDialog
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class StudentHomeworkListFragment :
    BaseFragment<FragmentStudentHomeworkListBinding, StudentHomeworkViewModel, StudentHomeworkState, Unit>(),
    ToolbarConfigProvider {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentHomeworkListBinding.inflate(inflater, container, false)

    override val viewModel: StudentHomeworkViewModel by viewModels()

    private lateinit var adapter: HomeworkAdapter

    override fun setupUI() {
        adapter = HomeworkAdapter(
            onItemClick = viewModel::onHomeWorkClick,
            onMediaDownload = viewModel::onMediaClick,
            onMediaDelete = null,
            isEditMode = false
        )
        binding.rvHomeworkList.adapter = adapter
    }

    private fun showFilterDialog() {
        if (parentFragmentManager.findFragmentByTag("filter") != null) return
        StudentHomeworkFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = viewModel::filterApplied,
            onError = viewModel.onError
        ).show(parentFragmentManager, "filter")
    }

    override fun updateState(state: StudentHomeworkState) {
        adapter.submitList(state.homeworkList)
    }

    override fun updateEvent(event: Unit) {}
    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = R.string.homework_assignments,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = {
            when (it.itemId) {
                R.id.action_filter -> showFilterDialog()
            }
        }
    )
}