package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentLessonMarksBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.utils.toUI
import stud.euktop.uikit.components.markPicker.SchJMarkPicker
import stud.euktop.uikit.components.markPicker.SchJMarkPickerSheet
import stud.euktop.uikit.components.markPicker.SchJMarkPickerState

@AndroidEntryPoint
class LessonMarksFragment : BaseFragment<
        FragmentLessonMarksBinding,
        LessonMarksViewModel,
        LessonMarksState,
        Unit
        >() {
    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentLessonMarksBinding.inflate(i, c, false)

    override val viewModel: LessonMarksViewModel by viewModels()
    private var adapter: LessonMarksAdapter? = null

    override fun setupUI() {
        adapter = LessonMarksAdapter { item ->
            SchJMarkPickerSheet(
                state = SchJMarkPickerState(
                    studentName = "${item.lastName} ${item.firstName}",
                    absenceTypes = item.absenceCode?.toUI()
                )
            ) { absenceType ->
                messages.message(MessageParam(R.string.mock_grade_saved) {})
            }.show(parentFragmentManager, "markPicker")
        }
        binding.rvMarks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMarks.adapter = adapter
    }

    override fun updateState(state: LessonMarksState) {
        adapter?.submitList(state.marks)
    }

    override fun updateEvent(event: Unit) {}
}