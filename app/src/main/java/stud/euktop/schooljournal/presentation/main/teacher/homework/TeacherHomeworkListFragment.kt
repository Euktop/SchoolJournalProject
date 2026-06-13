package stud.euktop.schooljournal.presentation.main.teacher.homework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.loger.logger
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherHomeworkListBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.filter.homework.HomeworkFilterDialog
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import stud.euktop.schooljournal.presentation.common.utils.submitList
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeworkListFragment : BaseFragment<
        FragmentTeacherHomeworkListBinding,
        TeacherHomeworkViewModel,
        TeacherHomeworkState,
        TeacherHomeworkEvent>(), ToolbarConfigProvider {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherHomeworkListBinding.inflate(inflater, container, false)

    override val viewModel: TeacherHomeworkViewModel by viewModels()

    @Inject
    lateinit var router: RouterTeacher

    private lateinit var adapter: TeacherHomeworkAdapter

    override fun setupUI() {
        binding.fabAddHomework.setOnClickListener { router.toTeacherHomeworkEdit() }

        adapter = TeacherHomeworkAdapter { homework ->
            router.toTeacherHomeworkEdit(homework.homeworkId)
        }
        binding.rvHomeworkList.adapter = adapter
    }

    override fun updateEvent(event: TeacherHomeworkEvent) {
        when (event) {
            TeacherHomeworkEvent.NavigateToAdd -> router.toTeacherHomeworkEdit()
            is TeacherHomeworkEvent.EditHomework -> router.toTeacherHomeworkEdit(event.homeworkId)
        }
    }

     override fun updateState(state: TeacherHomeworkState) {
         logger?.d(this::class.java.simpleName, "updateState", "homework list count: ${state.homeworkList.size}")
         binding.rvHomeworkList.submitList(state.homeworkList)
     }

    private fun showFilterDialog() {
        if (parentFragmentManager.findFragmentByTag("filter") != null) return
        val dialog = HomeworkFilterDialog(
            initialFilter = viewModel.state.value.homeworkFilter,
            onFilterApplied = { filter -> viewModel.applyFilter(filter) },
            onError = { error -> messages.message(MessageParam(error.messageId)) }
        )
        try {
            logger?.d(this::class.java.simpleName, "showFilterDialog", "showing homework filter")
        } catch (_: Throwable) {
        }
        dialog.show(parentFragmentManager, "filter")
    }

    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = R.string.homeworks,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = {
            when (it.itemId) {
                R.id.action_filter -> showFilterDialog()
            }
        }
    )
}