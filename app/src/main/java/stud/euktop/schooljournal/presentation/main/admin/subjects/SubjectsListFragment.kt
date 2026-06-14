package stud.euktop.schooljournal.presentation.main.admin.subjects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.utils.loger.logger
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.adapter.OperationsListAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.subject.SubjectFilterDialog
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class SubjectsListFragment : BaseFragment<
        FragmentAdminEntityBinding,
        SubjectsListViewModel,
        SubjectsListState,
        Unit>(), ToolbarConfigProvider {

    override val viewModel: SubjectsListViewModel by viewModels()
    private lateinit var loadingDelegate: LoadingDelegate<SubjectsListState>
    private lateinit var adapter: OperationsListAdapter<Subject>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        adapter = OperationsListAdapter(
            toText = { it.name },
            onEdit = { viewModel.editSubject(it) },
            onDelete = { viewModel.deleteSubject(it.subjectId) },
            showContextMenu = true,
            editOnClick = true
        )
        binding.rvEntity.adapter = adapter

        binding.rvEntity.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                if (!viewModel.isLoading("pagination") && viewModel.hasMore && lastVisible >= adapter.itemCount - 2) {
                    viewModel.loadNextPage()
                }
            }
        })

        binding.fabCreateNew.setOnClickListener { viewModel.createNew() }

        viewModel.loadNextPage()
    }

    private fun showFilterDialog() {
        val dialog = SubjectFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = { viewModel.applyFilter(it) },
            onError = viewModel.onError
        )
        try {
            logger?.d(this::class.java.simpleName, "showFilterDialog", "showing subject_filter")
        } catch (_: Throwable) {
        }
        dialog.show(childFragmentManager, "subject_filter")
    }

     override fun updateState(state: SubjectsListState) {
         logger?.d(this::class.java.simpleName, "updateState", "subjects count: ${state.subjects.size}")
         adapter.submitList(state.subjects)
         binding.rvEntity.update()
     }

    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig(): ToolbarConfig = ToolbarConfig(
        titleRes = R.string.subjects,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> showFilterDialog()
            }
        }
    )
}