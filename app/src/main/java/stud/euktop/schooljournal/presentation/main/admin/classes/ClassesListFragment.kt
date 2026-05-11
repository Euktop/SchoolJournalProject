package stud.euktop.schooljournal.presentation.main.admin.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.adapter.OperationsListAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.classes.ClassFilterDialog

@AndroidEntryPoint
class ClassesListFragment : BaseFragment<
        FragmentAdminEntityBinding,
        ClassesListViewModel,
        ClassesListState,
        Unit>() {

    override val viewModel: ClassesListViewModel by viewModels()
    private lateinit var loadingDelegate: LoadingDelegate<ClassesListState>
    private lateinit var adapter: OperationsListAdapter<ClassInfo>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        adapter = OperationsListAdapter(
            toText = { "${it.grade}${it.letter}" },
            onEdit = viewModel::editClass,
            onDelete = { viewModel.deleteClass(it.classId) },
            showContextMenu = true,
            editOnClick = true  // по клику на элемент - редактирование
        )
        binding.rvEntity.adapter = adapter

        binding.toolbar.setTitle(R.string.classes)
        binding.toolbar.showFilterDialog = { showFilterDialog() }

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
        ClassFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = { viewModel.applyFilter(it) },
            onError = viewModel.onError
        ).show(childFragmentManager, "class_filter")
    }

    override fun updateState(state: ClassesListState) {
        adapter.submitList(state.classes)
        binding.rvEntity.update()
    }

    override fun updateEvent(event: Unit) {}
}