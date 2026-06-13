package stud.euktop.schooljournal.presentation.common.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

class PaginationScrollListener(
    private val loadMore: () -> Unit,
    private val isLoadingProvider: () -> Boolean,
    private val isLastPageProvider: () -> Boolean,
    private val pageSize: Int = 20
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        val isLoading = isLoadingProvider()
        val isLastPage = isLastPageProvider()

        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= pageSize
            ) {
                try {
                    logger?.d(this.toSimpleTag(), "pagination", "loadMore triggered: visible=$visibleItemCount, first=$firstVisibleItemPosition, total=$totalItemCount")
                } catch (_: Throwable) {
                }
                loadMore()
            }
        }
    }
}