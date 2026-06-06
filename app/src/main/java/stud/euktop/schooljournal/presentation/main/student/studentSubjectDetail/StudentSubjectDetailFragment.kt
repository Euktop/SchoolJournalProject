// presentation/main/student/studentSubjectDetail/StudentSubjectDetailFragment.kt
package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.databinding.FragmentStudentSubjectDetailBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.filter.grade.GradeFilterDialog
import stud.euktop.uikit.components.lineChart.SchJLineChartState
import stud.euktop.uikit.components.lineChart.SchJLinePoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class StudentSubjectDetailFragment : BaseFragment<
        FragmentStudentSubjectDetailBinding,
        StudentSubjectDetailViewModel,
        StudentSubjectDetailState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentSubjectDetailBinding.inflate(inflater, container, false)

    override val viewModel: StudentSubjectDetailViewModel by viewModels()

    private lateinit var adapter: StudentMarkPagingAdapter
    private lateinit var loadingDelegate: LoadingDelegate<StudentSubjectDetailState>

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        adapter = StudentMarkPagingAdapter { /* клик по оценке не нужен */ }
        binding.rvMarks.adapter = adapter

        // Подписка на PagingData
        lifecycleScope.launch {
            viewModel.pagingDataFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        // Кнопка фильтра (можно добавить на toolbar)
        binding.toolbar.showFilterDialog = { showFilterDialog() }
        binding.toolbar.setupWithNavController(findNavController())
    }

    private fun showFilterDialog() {
        GradeFilterDialog(
            initialFilter = viewModel.filterFlow.value,
            onFilterApplied = { filter -> viewModel.applyFilter(filter) },
            onError = viewModel.onError
        ).show(childFragmentManager, "grade_filter")
    }

    @SuppressLint("SimpleDateFormat")
    override fun updateState(state: StudentSubjectDetailState) {
        binding.lcProgress.state = SchJLineChartState(
            points = state.aggregatedMarks.sortedBy { it.date }.map { aggregated ->
                SchJLinePoint(
                    label = SimpleDateFormat("dd.MM", Locale.getDefault()).format(aggregated.date),
                    value = aggregated.averageMark?.toFloat() ?: 0f
                )
            }
        )
    }

    override fun updateEvent(event: Unit) {}
}