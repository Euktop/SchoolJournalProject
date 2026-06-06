package stud.euktop.schooljournal.presentation.main.student.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentStudentScheduleBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.utils.submitList

@AndroidEntryPoint
class StudentScheduleFragment : BaseFragment<
        FragmentStudentScheduleBinding,
        StudentScheduleViewModel,
        StudentScheduleState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentScheduleBinding.inflate(inflater, container, false)

    override val viewModel: StudentScheduleViewModel by viewModels()

    private lateinit var adapter: StudentScheduleAdapter

    override fun setupUI() {
        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.showFilterDialog = {
            // Позже можно добавить фильтр по дате
        }

        adapter = StudentScheduleAdapter { item ->
            // Можно открыть детали урока, если нужно
        }
        binding.rvSchedule.adapter = adapter
    }

    override fun updateState(state: StudentScheduleState) {
        binding.rvSchedule.submitList(state.schedule)
    }

    override fun updateEvent(event: Unit) {}
}