package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.databinding.FragmentLessonMarksBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import stud.euktop.schooljournal.presentation.common.utils.submitList
import stud.euktop.schooljournal.presentation.common.utils.toDomain
import stud.euktop.schooljournal.presentation.common.utils.toUI
import stud.euktop.uikit.R
import stud.euktop.uikit.components.markPicker.SchJMarkPickerSheet
import stud.euktop.uikit.components.markPicker.SchJMarkPickerState
import javax.inject.Inject

@AndroidEntryPoint
class LessonMarksFragment : BaseFragment<
        FragmentLessonMarksBinding,
        LessonMarksViewModel,
        LessonMarksState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLessonMarksBinding.inflate(inflater, container, false)

    override val viewModel: LessonMarksViewModel by viewModels()

    @Inject
    lateinit var router: RouterTeacher

    private lateinit var marksAdapter: LessonMarksAdapter
    private lateinit var chipAdapter: ChipAdapter

    override fun setupUI() {
        // Кнопка назад
        binding.btnBack.setOnClickListener { lifecycleScope.launch { router.toBack() } }

        // Чипы
        val chips = listOf(
            getString(R.string.chip_presence),
            getString(R.string.chip_grades),
            getString(R.string.chip_homework)
        )
        chipAdapter = ChipAdapter(chips) { index ->
            viewModel.selectChip(index)
            chipAdapter.select(index)
        }
        binding.rvChips.adapter = chipAdapter

        // Список учеников
        marksAdapter = LessonMarksAdapter(
            onItemClick = { item -> showMarkPicker(item) },
            onEditClick = { item -> showMarkPicker(item) }
        )
        binding.rvMarks.adapter = marksAdapter

        // FAB
        binding.fabAdd.setOnClickListener {
            // TODO: Добавить действие для FAB (например, массовое выставление оценок или скролл вниз)
        }
    }

    private fun showMarkPicker(item: stud.euktop.domain.model.attendance.StudentMarkItem) {
        SchJMarkPickerSheet(
            state = SchJMarkPickerState(
                studentName = "${item.lastName} ${item.firstName}",
                absenceTypes = item.absenceCode?.toUI()
            )
        ) { absenceType ->
            val domainType = absenceType.toDomain()
            viewModel.saveGrade(
                studentId = item.studentId,
                absenceTypes = domainType,
                comment = null
            )
        }.show(parentFragmentManager, "markPicker")
    }

    override fun updateState(state: LessonMarksState) {
        binding.tvClassSubject.text = state.classAndSubject
        binding.rvMarks.submitList(state.marks)
    }

    override fun updateEvent(event: Unit) {}
}