package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.attendance.StudentMarkItem
import stud.euktop.schooljournal.databinding.FragmentLessonMarksBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import stud.euktop.schooljournal.presentation.common.utils.submitList
import stud.euktop.schooljournal.presentation.common.utils.toDomain
import stud.euktop.schooljournal.presentation.common.utils.toUI
import stud.euktop.uikit.components.markPicker.SchJMarkPickerSheet
import stud.euktop.uikit.components.markPicker.SchJMarkPickerState

@AndroidEntryPoint
class LessonMarksFragment : BaseFragment<
        FragmentLessonMarksBinding,
        LessonMarksViewModel,
        LessonMarksState,
        Unit>(), ToolbarConfigProvider {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLessonMarksBinding.inflate(inflater, container, false)

    override val viewModel: LessonMarksViewModel by viewModels()

    private lateinit var marksAdapter: LessonMarksAdapter

    override fun setupUI() {
        marksAdapter = LessonMarksAdapter(
            onItemClick = { item -> showMarkPicker(item) },
            onEditClick = { item -> showMarkPicker(item) }
        )
        binding.rvMarks.adapter = marksAdapter

    }

    private fun showMarkPicker(item: StudentMarkItem) {
        SchJMarkPickerSheet(
            state = SchJMarkPickerState(
                studentName = "${item.lastName} ${item.firstName}",
                absenceTypes = item.absenceCode?.toUI()
            )
        ) { absenceType ->
            val domainType = absenceType?.toDomain()
            viewModel.saveGrade(
                studentId = item.studentId,
                absenceTypes = domainType,
                comment = null
            )
        }.show(parentFragmentManager, "markPicker")
    }

    override fun updateState(state: LessonMarksState) {
        binding.rvMarks.submitList(state.marks)
    }

    override fun updateEvent(event: Unit) {}
    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = stud.euktop.schooljournal.R.string.grade_for_lesson,
    )
}