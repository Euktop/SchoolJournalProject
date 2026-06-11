package stud.euktop.schooljournal.presentation.main.student.homework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentStudentHomeworkListBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import javax.inject.Inject

@AndroidEntryPoint
class StudentHomeworkListFragment :
    BaseFragment<FragmentStudentHomeworkListBinding, StudentHomeworkViewModel, StudentHomeworkState, Unit>(),
    ToolbarConfigProvider {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentHomeworkListBinding.inflate(inflater, container, false)

    override val viewModel: StudentHomeworkViewModel by viewModels()

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var adapter: StudentHomeworkAdapter

    override fun setupUI() {
        adapter = StudentHomeworkAdapter {}
        binding.rvHomeworkList.adapter = adapter
    }

    private fun showFilterDialog() {
        val currentFilter = viewModel.state.value.filter
        val dialog = StudentHomeworkFilterDialog(
            initialFilter = currentFilter, onFilterApplied = { filter ->
                viewModel.filterApplied(filter)
            }, onError = viewModel.onError
        )
        dialog.show(parentFragmentManager, "filter")
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