package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentStudentSubjectDetailBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterStudent
import javax.inject.Inject

@AndroidEntryPoint
class StudentSubjectDetailFragment : BaseFragment<
        FragmentStudentSubjectDetailBinding,
        StudentSubjectDetailViewModel,
        StudentSubjectDetailState,
        Unit>() {

    @Inject
    internal lateinit var router: RouterStudent

    private val gradeAdapter by lazy {
        StudentSubjectGradeAdapter { viewModel.onGradeClick(it) }
    }

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentStudentSubjectDetailBinding.inflate(i, c, false)

    override val viewModel: StudentSubjectDetailViewModel by viewModels()

    override fun setupUI() {
        binding.rvGrades.adapter = gradeAdapter

        binding.toolbar.setNavigationOnClickListener { viewModel.onBackClick() }
        binding.tvAllGrades.setOnClickListener { viewModel.onAllGradesClick() }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let(viewModel::onTabSelected)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun updateState(state: StudentSubjectDetailState) {
        binding.toolbar.title = state.subjectName
        binding.tvTeacherName.text = state.teacherName
        binding.tvAverageMark.text = state.averageMark
        binding.tvTrend.text = state.trend
        binding.tvNextLesson.text = state.nextLesson

        binding.containerGrades.visibility = if (state.selectedTab == 0) View.VISIBLE else View.GONE
        binding.containerHomework.visibility =
            if (state.selectedTab == 1) View.VISIBLE else View.GONE

        gradeAdapter.submitList(state.grades)
    }

    override fun updateEvent(event: Unit) {}
}