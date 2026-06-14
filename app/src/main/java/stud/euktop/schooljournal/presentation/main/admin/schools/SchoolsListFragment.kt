package stud.euktop.schooljournal.presentation.main.admin.schools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.School
import stud.euktop.domain.utils.loger.logger
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.adapter.OperationsListAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.school.SchoolFilterDialog
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class SchoolsListFragment : BaseFragment<
        FragmentAdminEntityBinding,
        SchoolsListViewModel,
        SchoolsListState,
        Unit>(), ToolbarConfigProvider {

    override val viewModel: SchoolsListViewModel by viewModels()
    private lateinit var loadingDelegate: LoadingDelegate<SchoolsListState>
    private lateinit var adapter: OperationsListAdapter<School>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        adapter = OperationsListAdapter(
            toText = { it.name },
            onEdit = { viewModel.editSchool(it) },
            onDelete = { viewModel.deleteSchool(it.schoolId) },
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
        val dialog = SchoolFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = { viewModel.applyFilter(it) },
            onError = viewModel.onError
        )
        try {
            logger?.d(this::class.java.simpleName, "showFilterDialog", "showing school_filter")
        } catch (_: Throwable) {
        }
        dialog.show(childFragmentManager, "school_filter")
    }

     override fun updateState(state: SchoolsListState) {
         logger?.d(this::class.java.simpleName, "updateState", "schools count: ${state.schools.size}")
         adapter.submitList(state.schools)
         binding.rvEntity.update()
     }

    override fun updateEvent(event: Unit) {}
    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = R.string.schools,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = {
            when (it.itemId) {
                R.id.action_filter -> showFilterDialog()
            }
        }
    )
}