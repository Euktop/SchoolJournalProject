package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentLessonMarksBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
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
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLessonMarksBinding.inflate(inflater, container, false)

    override val viewModel: LessonMarksViewModel by viewModels()

    override fun setupUI() {
        binding.rvMarks.adapter = LessonMarksAdapter { item ->
            SchJMarkPickerSheet(
                state = SchJMarkPickerState(
                    studentName = "${item.lastName} ${item.firstName}",
                    absenceTypes = item.absenceCode?.toUI()
                )
            ) { absenceType ->
                // absenceType – это uikit-тип, преобразуем в domain
                val domainType = absenceType.toDomain()
                viewModel.saveGrade(
                    studentId = item.studentId,
                    absenceTypes = domainType,
                    comment = null   // можно добавить поле для комментария
                )
            }.show(parentFragmentManager, "markPicker")
        }
    }

    override fun updateState(state: LessonMarksState) {
        binding.rvMarks.submitList(state.marks)
    }

    override fun updateEvent(event: Unit) {}
}