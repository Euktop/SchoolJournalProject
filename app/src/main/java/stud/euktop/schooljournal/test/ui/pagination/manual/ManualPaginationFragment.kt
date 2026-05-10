package stud.euktop.schooljournal.test.ui.pagination.manual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.ActivityTestPaginationBinding
import stud.euktop.schooljournal.databinding.FragmentManualPaginationBinding
import stud.euktop.schooljournal.test.data.MockItemRepository

class ManualPaginationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ManualItemAdapter
    private var isLoading = false
    private var currentPage = 0
    private val pageSize = 20
    private var hasMoreData = true
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

        adapter = ManualItemAdapter()
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && hasMoreData) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= pageSize
                    ) {
                        loadNextPage()
                    }
                }
            }
        })

        loadNextPage()
    }

    private fun loadNextPage() {
        if (isLoading) return
        isLoading = true
        progressBar.visibility = ProgressBar.VISIBLE
        if (hasMoreData) adapter.addLoadingFooter()

        lifecycleScope.launch(Dispatchers.IO) {
            val newItems = MockItemRepository.getItems(currentPage, pageSize)
            withContext(Dispatchers.Main) {
                adapter.removeLoadingFooter()
                if (newItems.isNotEmpty()) {
                    adapter.submitList(newItems)
                    currentPage++
                    // Главное изменение:
                    hasMoreData = newItems.size == pageSize
                } else {
                    hasMoreData = false
                }
                isLoading = false
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }
}