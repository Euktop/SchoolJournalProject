package stud.euktop.schooljournal.test.ui.pagination.auto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.databinding.ActivityTestPaginationBinding
import stud.euktop.schooljournal.test.data.MockItemRepository

class AutoPaginationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: AutoItemAdapter
    private lateinit var binding: ActivityTestPaginationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityTestPaginationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        progressBar = binding.progressBar

        adapter = AutoItemAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val pagingSource = object : PagingSource<Int, String>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
                val page = params.key ?: 0
                return try {
                    val items = MockItemRepository.getItems(page, params.loadSize)
                    LoadResult.Page(
                        data = items,
                        prevKey = if (page > 0) page - 1 else null,
                        nextKey = if (items.size == params.loadSize) page + 1 else null
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, String>): Int? {
                return state.anchorPosition?.let { anchor ->
                    state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
                }
            }
        }
        adapter.addLoadStateListener { loadState ->
            val isLoading = loadState.refresh is LoadState.Loading ||
                    loadState.append is LoadState.Loading
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        val pager = Pager(
            config = PagingConfig(pageSize = 20, maxSize = 60, enablePlaceholders = false),
            pagingSourceFactory = { pagingSource }
        )

        lifecycleScope.launch {
            pager.flow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
                progressBar.visibility = View.GONE
            }
        }
    }
}