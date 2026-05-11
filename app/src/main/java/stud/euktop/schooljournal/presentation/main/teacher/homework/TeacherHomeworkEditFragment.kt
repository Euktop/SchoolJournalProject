package stud.euktop.schooljournal.presentation.main.teacher.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.lesson.LessonFull
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.databinding.FragmentTeacherHomeworkEditBinding
import stud.euktop.schooljournal.presentation.common.adapter.ListSelectAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindForm
import stud.euktop.schooljournal.presentation.common.binding.toInit
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.check
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeworkEditFragment : BaseFragment<
        FragmentTeacherHomeworkEditBinding,
        TeacherHomeworkViewModel,
        TeacherHomeworkState,
        TeacherHomeworkEvent>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherHomeworkEditBinding.inflate(inflater, container, false)

    override val viewModel: TeacherHomeworkViewModel by viewModels()
    private val focusTrack = FocusTrack()
    private lateinit var loadingDelegate: LoadingDelegate<TeacherHomeworkState>

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var lessonAdapter: ListSelectAdapter<LessonFull>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeworkId = arguments?.getInt("homeworkId", 0) ?: 0
        if (homeworkId != 0) {
            viewModel.setEditMode(homeworkId)
        }
    }

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        bindForm(focusTrack, viewModel) {
            field(binding.inputDescription, { it.description }, viewModel::updateDescription)
        }

        lessonAdapter = ListSelectAdapter(
            toText = { lesson -> "${lesson?.subject?.name} - ${lesson?.startTime}" },
            onItemSelected = { lesson -> viewModel.selectLesson(lesson?.lessonId) }
        )
        binding.selectLesson.attach(lessonAdapter, lessonAdapter, childFragmentManager)

        binding.selectLesson.onShowing = {
            lessonAdapter.submitList(viewModel.state.value.availableLessons)
        }

        binding.buttonsSaveCancel.toInit(loadingDelegate, viewModel::save, viewModel::cancel)
    }

    override fun updateState(state: TeacherHomeworkState) {
        binding.inputDescription.check(focusTrack, state.description)

        val selectedText = state.selectedLesson?.let {
            "${it.subject.name} - ${it.startTime} (${it.date.toBaseString()})"
        } ?: ""
        binding.selectLesson.state = binding.selectLesson.state.copy(selectText = selectedText)

        binding.buttonsSaveCancel.btnSave.isEnabled = state.isFormValid() && !state.isAnyLoading()
    }

    override fun updateEvent(event: TeacherHomeworkEvent) {
        when (event) {
            TeacherHomeworkEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
            else -> {}
        }
    }
}