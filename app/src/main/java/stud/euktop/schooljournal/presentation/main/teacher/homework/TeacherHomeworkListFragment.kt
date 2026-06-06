package stud.euktop.schooljournal.presentation.main.teacher.homework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentTeacherHomeworkListBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.filter.homework.HomeworkFilterDialog
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import stud.euktop.schooljournal.presentation.common.utils.submitList
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeworkListFragment : BaseFragment<
        FragmentTeacherHomeworkListBinding,
        TeacherHomeworkViewModel,
        TeacherHomeworkState,
        TeacherHomeworkEvent>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherHomeworkListBinding.inflate(inflater, container, false)

    override val viewModel: TeacherHomeworkViewModel by viewModels()

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject lateinit var router: RouterTeacher

    private lateinit var adapter: TeacherHomeworkAdapter

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { router.navigateBack() }
        binding.fabAddHomework.setOnClickListener { router.toTeacherHomeworkEdit() }

        adapter = TeacherHomeworkAdapter { homework ->
            router.toTeacherHomeworkEdit(homework.homeworkId)
        }
        binding.toolbar.showFilterDialog = { showFilterDialog() }
        binding.rvHomeworkList.adapter = adapter
    }

    override fun updateEvent(event: TeacherHomeworkEvent) {
        when (event) {
            TeacherHomeworkEvent.NavigateToAdd -> router.toTeacherHomeworkEdit()
            is TeacherHomeworkEvent.EditHomework -> router.toTeacherHomeworkEdit(event.homeworkId)
            TeacherHomeworkEvent.NavigateBack -> router.navigateBack()
        }
    }

    override fun updateState(state: TeacherHomeworkState) {
        binding.rvHomeworkList.submitList(state.homeworkList)
    }

    private fun showFilterDialog() {
        if (parentFragmentManager.findFragmentByTag("filter") != null) return
        val dialog = HomeworkFilterDialog(
            initialFilter = viewModel.state.value.homeworkFilter,
            onFilterApplied = { filter -> viewModel.applyFilter(filter) },
            onError = { error -> messages.message(MessageParam(error.messageId)) }
        )
        dialog.show(parentFragmentManager, "filter")
    }
}