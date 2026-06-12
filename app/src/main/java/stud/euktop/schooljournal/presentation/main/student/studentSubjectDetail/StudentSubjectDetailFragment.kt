package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.databinding.FragmentStudentSubjectDetailBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterStudent
import stud.euktop.uikit.R
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class StudentSubjectDetailFragment :
    BaseFragment<FragmentStudentSubjectDetailBinding, StudentSubjectDetailViewModel, StudentSubjectDetailState, Unit>() {

    @Inject
    internal lateinit var router: RouterStudent

    private lateinit var marksAdapter: StudentMarkPagingAdapter

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentSubjectDetailBinding.inflate(inflater, container, false)

    override val viewModel: StudentSubjectDetailViewModel by viewModels()

    override fun setupUI() {
        marksAdapter = StudentMarkPagingAdapter()
        binding.rvGrades.adapter = marksAdapter

        binding.toolbar.setNavigationOnClickListener { viewModel.onBackClick() }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                state.marksPagingDataFlow?.collectLatest { pagingData ->
                    marksAdapter.submitData(pagingData)
                }
            }
        }
    }

    override fun updateState(state: StudentSubjectDetailState) {
        binding.toolbar.title = state.subjectSummary?.subjectName ?: ""

        val teacherName = state.subjectSummary?.teacherName
        binding.headerInclude.tvTeacherName.text = teacherName?.ifEmpty { null }
            ?: getString(R.string.subject_detail_teacher_not_specified)

        binding.headerInclude.tvAverageMark.text = state.overallAverage?.let {
            String.format(Locale.getDefault(), "%.2f", it)
        } ?: getString(R.string.subject_detail_trend_not_available)

        binding.headerInclude.tvTrend.text = state.trendFormatted
        binding.headerInclude.tvTrend.visibility =
            if (state.trendFormatted.isEmpty()) View.GONE else View.VISIBLE

        val nextLesson = state.scheduleItems.firstOrNull()
        binding.headerInclude.tvNextLesson.text = nextLesson?.let {
            "${it.startTime} - ${it.subjectName}"
        } ?: getString(R.string.subject_detail_no_next_lesson)
    }

    override fun updateEvent(event: Unit) {}
}