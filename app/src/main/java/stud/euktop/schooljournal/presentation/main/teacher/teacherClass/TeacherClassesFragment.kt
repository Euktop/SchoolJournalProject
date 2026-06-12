package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherClassesBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.utils.submitList

@AndroidEntryPoint
class TeacherClassesFragment : BaseFragment<
        FragmentTeacherClassesBinding,
        TeacherClassesViewModel,
        TeacherClassesState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherClassesBinding.inflate(inflater, container, false)

    override val viewModel: TeacherClassesViewModel by viewModels()

    override fun setupUI() {
        binding.rvClasses.adapter = TeacherClassAdapter(viewModel::onClassClick)
    }

    override fun updateState(state: TeacherClassesState) {
        val greeting = if (state.teacherName.isNotEmpty()) {
            getString(R.string.good_day_format, state.teacherName)
        } else {
            getString(R.string.good_day)
        }
        binding.tvGreeting.text = greeting

        val lessonsCount = state.classes.size
        binding.tvLessonsCount.text = getString(R.string.lessons_today_format, lessonsCount)

        binding.rvClasses.submitList(state.classes)
    }

    override fun updateEvent(event: Unit) {}
}