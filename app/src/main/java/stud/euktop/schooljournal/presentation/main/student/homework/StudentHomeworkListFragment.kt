package stud.euktop.schooljournal.presentation.main.student.homework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentStudentHomeworkListBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@AndroidEntryPoint
class StudentHomeworkListFragment :
    BaseFragment<FragmentStudentHomeworkListBinding, StudentHomeworkViewModel, StudentHomeworkState, Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentHomeworkListBinding.inflate(inflater, container, false)

    override val viewModel: StudentHomeworkViewModel by viewModels()

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var adapter: StudentHomeworkAdapter

    override fun setupUI() {
        binding.toolbar.showFilterDialog = { showFilterDialog() }

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
}