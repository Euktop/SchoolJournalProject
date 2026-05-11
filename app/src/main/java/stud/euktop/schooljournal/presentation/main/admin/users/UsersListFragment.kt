package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.user.UserFilterDialog

@AndroidEntryPoint
class UsersListFragment : BaseFragment<
        FragmentAdminEntityBinding,
        UsersListViewModel,
        UsersListState,
        Unit>() {

    override val viewModel: UsersListViewModel by viewModels()
    private lateinit var loadingDelegate: LoadingDelegate<UsersListState>
    private lateinit var adapter: UsersListAdapter
    private lateinit var scrollListener: PaginationScrollListener

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        adapter = UsersListAdapter(
            onEditClick = { viewModel.editUser(it) },
            onDeleteClick = { viewModel.deleteUser(it.userId) }
        )
        binding.rvEntity.adapter = adapter

        // Фильтр
        binding.toolbar.showFilterDialog = { showFilterDialog() }

        // Пагинация
        scrollListener = PaginationScrollListener { viewModel.loadNextPage() }
        binding.rvEntity.recyclerView.addOnScrollListener(scrollListener)

        // Загружаем первую страницу
        viewModel.loadNextPage()
    }

    override fun updateState(state: UsersListState) {
        adapter.submitList(state.users)
        binding.rvEntity.update() // обновляем empty view
    }

    private fun showFilterDialog() {
        if (parentFragmentManager.findFragmentByTag("user_filter") != null) return
        val dialog = UserFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = { viewModel.applyFilter(it) },
            onError = viewModel.onError
        )
        dialog.show(parentFragmentManager, "user_filter")
    }

    override fun updateEvent(event: Unit) {}
}