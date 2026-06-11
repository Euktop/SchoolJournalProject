package stud.euktop.schooljournal.presentation.main.admin.rooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.adapter.OperationsListAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

@AndroidEntryPoint
class RoomsListFragment : BaseFragment<
        FragmentAdminEntityBinding,
        RoomsListViewModel,
        RoomsListState,
        Unit>(), ToolbarConfigProvider {

    override val viewModel: RoomsListViewModel by viewModels()
    private lateinit var loadingDelegate: LoadingDelegate<RoomsListState>
    private lateinit var adapter: OperationsListAdapter<Room>

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        adapter = OperationsListAdapter(
            toText = { it.name },
            onEdit = { viewModel.editRoom(it) },
            onDelete = { viewModel.deleteRoom(it.roomId) },
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

    private class RoomFilterDialog(
        initialFilter: RoomFilter,
        onFilterApplied: (RoomFilter) -> Unit,
        onError: (CoordinatorResult.Error) -> Unit
    ) : DialogFragment()

    private fun showFilterDialog() {
        RoomFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = { viewModel.applyFilter(it) },
            onError = viewModel.onError
        ).show(childFragmentManager, "room_filter")
    }

    override fun updateState(state: RoomsListState) {
        adapter.submitList(state.rooms)
        binding.rvEntity.update()
    }

    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig(): ToolbarConfig = ToolbarConfig(
        titleRes = stud.euktop.uikit.R.string.rooms,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> showFilterDialog()
            }
        }
    )
}