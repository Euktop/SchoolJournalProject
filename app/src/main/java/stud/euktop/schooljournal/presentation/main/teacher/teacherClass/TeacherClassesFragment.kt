package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherClassesBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import stud.euktop.schooljournal.presentation.common.utils.submitList
import javax.inject.Inject

@AndroidEntryPoint
class TeacherClassesFragment : BaseFragment<
        FragmentTeacherClassesBinding,
        TeacherClassesViewModel,
        TeacherClassesState,
        Unit>() {

    @Inject
    internal lateinit var navigationManager: NavigationManager

    @Inject
    internal lateinit var router: RouterTeacher

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherClassesBinding.inflate(i, c, false)

    override val viewModel: TeacherClassesViewModel by viewModels()

    override fun setupUI() {
        binding.rvClasses.adapter = TeacherClassAdapter { item ->
            router.toTeacherLessons(item.classId, item.subjectId)
        }
    }

    override fun updateState(state: TeacherClassesState) {
        // Обновляем приветствие
        val greeting = if (state.teacherName.isNotEmpty()) {
            getString(R.string.good_day_format, state.teacherName)
        } else {
            getString(R.string.good_day)
        }
        binding.tvGreeting.text = greeting

        // Обновляем количество занятий (пока заглушка)
        val lessonsCount = state.classes.size
        binding.tvLessonsCount.text = getString(R.string.lessons_today_format, lessonsCount)

        // Обновляем список классов
        binding.rvClasses.submitList(state.classes)
    }

    override fun updateEvent(event: Unit) {}
}