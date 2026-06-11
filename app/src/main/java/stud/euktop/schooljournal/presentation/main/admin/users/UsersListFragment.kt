package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.adapter.OperationsListAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.user.UserFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.user.toApp
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class UsersListFragment : BaseFragment<
        FragmentAdminEntityBinding,
        UsersListViewModel,
        UsersListState,
        Unit>(), ToolbarConfigProvider {

    override val viewModel: UsersListViewModel by viewModels()
    private lateinit var loadingDelegate: LoadingDelegate<UsersListState>
    private lateinit var adapter: OperationsListAdapter<UserListItem>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        adapter = OperationsListAdapter(
            toText = { "${it.lastName} ${it.firstName} (${it.email})" },
            onEdit = { viewModel.editUser(it) },
            onDelete = { viewModel.deleteUser(it.userId) },
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

        viewModel.loadNextPage()
    }

    private fun showFilterDialog() {
        lifecycleScope.launch {
            viewModel.apply {
                val school = getSchoolUserFilter()
                UserFilterDialog(
                    initialFilter = viewModel.state.value.filter.toApp(school),
                    onFilterApplied = { viewModel.applyFilter(it.toDomainFilter()) },
                    onError = viewModel.onError
                ).show(childFragmentManager, "user_filter")
            }
        }
    }

    override fun updateState(state: UsersListState) {
        adapter.submitList(state.users)
        binding.rvEntity.update()
    }

    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig(): ToolbarConfig = ToolbarConfig(
        titleRes = R.string.users,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> showFilterDialog()
            }
        }
    )
}