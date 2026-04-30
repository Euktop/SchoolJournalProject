package stud.euktop.schooljournal.presentation.main.teacher.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.lesson.Lesson
import stud.euktop.domain.utils.toBaseString
import stud.euktop.uikit.R as R1
import stud.euktop.schooljournal.databinding.FragmentTeacherHomeworkEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.*
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeworkEditFragment : BaseFragment<
        FragmentTeacherHomeworkEditBinding,
        TeacherHomeworkViewModel,
        TeacherHomeworkState,
        TeacherHomeworkEvent>() {

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherHomeworkEditBinding.inflate(i, c, false)

    override val viewModel: TeacherHomeworkViewModel by viewModels()

    private val focusTrack = FocusTrack()

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var lessonRegister: SchJSearchableSelect.RegisterList<Lesson, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeworkId = arguments?.getInt("homeworkId", 0) ?: 0
        if (homeworkId != 0) {
            viewModel.setEditMode(homeworkId)
        }
    }

    override fun setupUI() {
        binding.apply {
            inputDescription.setup(focusTrack) { viewModel.updateDescription(it) }
            inputAttachedFiles.setup(focusTrack) { viewModel.updateAttachedFiles(it) }

            lessonRegister = selectLesson.RegisterList(
                items = ListSafe(
                    toText = { lesson ->
                        "${lesson.subject.name} - ${lesson.classInfo.name} (${lesson.date.toBaseString()})"
                    },
                    onClick = { lesson, _ -> viewModel.selectLesson(lesson.lessonId) }
                ),
                onSearchQueryChanged = { query ->
                    val filtered = viewModel.state.value.availableLessons.filter { lesson ->
                        lesson.subject.name.contains(query, ignoreCase = true) ||
                                lesson.classInfo.name.contains(query, ignoreCase = true)
                    }
                    lessonRegister.updateItems(filtered)
                }
            )
            lessonRegister.register(childFragmentManager)

            buttonsSaveCancel.btnSave.setOnClickListener {
                val selectedLesson =
                    viewModel.state.value.selectedLesson ?: return@setOnClickListener
                if (viewModel.state.value.isEditMode) {
                    viewModel.updateHomework(
                        homeworkId = viewModel.state.value.editingHomeworkId,
                        description = inputDescription.state.text,
                        attachedFiles = inputAttachedFiles.state.text,
                        lessonId = selectedLesson.lessonId
                    )
                } else {
                    viewModel.addHomework(
                        description = inputDescription.state.text,
                        attachedFiles = inputAttachedFiles.state.text,
                        lessonId = selectedLesson.lessonId
                    )
                }
            }
            buttonsSaveCancel.btnCancel.setOnClickListener {
                navigationManager.navigate(NavCommand.Back)
            }
        }
    }

    override fun updateState(state: TeacherHomeworkState) {
        binding.apply {
            inputDescription.check(focusTrack, state.description)

            lessonRegister.updateItems(state.availableLessons)

            val selectedLessonText = state.selectedLesson?.let { lesson ->
                "${lesson.subject.name} - ${lesson.classInfo.name} (${lesson.date.toBaseString()})"
            } ?: ""
            selectLesson.state = selectLesson.state.copy(
                title = getString(R1.string.lesson),
                selectText = selectedLessonText
            )

            if (state.isEditMode) {
                inputDescription.state =
                    inputDescription.state.copy(text = state.description.value ?: "")
                inputAttachedFiles.state = inputAttachedFiles.state.copy(text = state.attachedFiles)
            }

            buttonsSaveCancel.btnSave.isEnabled = state.isFormValid() && !state.isLoading
        }
    }

    override fun updateEvent(event: TeacherHomeworkEvent) {
        when (event) {
            TeacherHomeworkEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
            else -> {}
        }
    }
}