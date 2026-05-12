package stud.euktop.schooljournal.presentation.main.admin.assignments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.adapter.OperationsListAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.assignment.TeacherAssignmentFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.assignment.toApp

@AndroidEntryPoint
class AssignmentsListFragment : BaseFragment<
        FragmentAdminEntityBinding,
        AssignmentsListViewModel,
        AssignmentsListState,
        Unit>() {

    override val viewModel: AssignmentsListViewModel by viewModels()
    private lateinit var loadingDelegate: LoadingDelegate<AssignmentsListState>
    private lateinit var adapter: OperationsListAdapter<TeacherAssignment>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        adapter = OperationsListAdapter(
            toText = { "Учитель ${it.assignmentId.teacherId} → Класс ${it.assignmentId.classId}" }, // упрощённо
            onEdit = viewModel::editAssignment,
            onDelete = { viewModel.deleteAssignment(it.assignmentId) },
            showContextMenu = true,
            editOnClick = true
        )
        binding.rvEntity.adapter = adapter

        binding.toolbar.setTitle(R.string.assignments)
        binding.toolbar.showFilterDialog = { showFilterDialog() }
        binding.toolbar.setupWithNavController(findNavController())

        binding.rvEntity.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                if (!viewModel.isLoading("pagination") && viewModel.hasMore && lastVisible >= adapter.itemCount - 2) {
                    viewModel.loadNextPage()
                }
            }
        })

        viewModel.loadNextPage()
    }

    private fun showFilterDialog() {
        TeacherAssignmentFilterDialog(
            //Нужно сделать то же самое, что и с UserFilter
            initialFilter = viewModel.state.value.filter.toApp(null, null, null),
            onFilterApplied = { viewModel.applyFilter(it.toDomain()) },
            onError = viewModel.onError
        ).show(childFragmentManager, "assignment_filter")
    }

    override fun updateState(state: AssignmentsListState) {
        adapter.submitList(state.assignments)
        binding.rvEntity.update()
    }

    override fun updateEvent(event: Unit) {}
}