package stud.euktop.schooljournal.presentation.main.student.homework

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentStudentHomeworkListBinding
import stud.euktop.schooljournal.presentation.common.adapter.HomeworkAdapter
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.filter.studenthomework.StudentHomeworkFilterDialog
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.navigate.contract.ErrorHandler
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import java.io.File
import javax.inject.Inject
import stud.euktop.uikit.R as RUi
import stud.euktop.schooljournal.presentation.common.notification.DownloadNotificationHelper
import com.google.android.material.snackbar.Snackbar

@AndroidEntryPoint
class StudentHomeworkListFragment :
    BaseFragment<FragmentStudentHomeworkListBinding, StudentHomeworkViewModel, StudentHomeworkState, StudentHomeworkEvent>(),
    ToolbarConfigProvider {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentHomeworkListBinding.inflate(inflater, container, false)

    override val viewModel: StudentHomeworkViewModel by viewModels()

    private lateinit var adapter: HomeworkAdapter

    override fun setupUI() {
        adapter = HomeworkAdapter(
            onItemClick = viewModel::onHomeWorkClick,
            onMediaDownload = viewModel::onMediaClick,
            onMediaDelete = null,
            isEditMode = false
        )
        binding.rvHomeworkList.adapter = adapter
    }

    private fun showFilterDialog() {
        if (parentFragmentManager.findFragmentByTag("filter") != null) return
        val dialog = StudentHomeworkFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = viewModel::filterApplied,
            onError = viewModel.onError
        )
        try {
            logger?.d(this::class.java.simpleName, "showFilterDialog", "showing student homework filter")
        } catch (_: Throwable) {
        }
        dialog.show(parentFragmentManager, "filter")
    }

     override fun updateState(state: StudentHomeworkState) {
         logger?.d(this::class.java.simpleName, "updateState", "homework list count: ${state.homeworkList.size}")
         adapter.submitList(state.homeworkList)
     }

    override fun updateEvent(event: StudentHomeworkEvent) {
        when (event) {
            is StudentHomeworkEvent.ShowHomeworkDetail -> showHomeworkDialog(event.homework)
            is StudentHomeworkEvent.DownloadMediaFile -> {
                // Обновляем уведомление о скачивании и показываем Snackbar с местом сохранения
                try {
                    DownloadNotificationHelper.showDownloaded(requireContext(), event.file)
                } catch (_: Throwable) {
                }
                Snackbar.make(
                    binding.root,
                    getString(RUi.string.download_saved_to, event.file.absolutePath),
                    requireContext().resources.getInteger(R.integer.duration_notification)
                ).setAction("Открыть") {
                    openMediaFile(event.file)
                }.show()
                // Не открываем файл автоматически — оставляем пользователю открыть через Snackbar или уведомление
            }
        }
    }

    private fun showHomeworkDialog(homework: stud.euktop.domain.model.homework.HomeworkFull) {
        AlertDialog.Builder(requireContext())
            .setTitle(RUi.string.homework_detail_title)
            .setMessage(
                "${homework.description}\n\n" +
                        getString(
                            RUi.string.homework_date_format,
                            homework.createdAt.toBaseString()
                        )
            )
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    @Inject
    internal lateinit var errorHandler: ErrorHandler

    private fun openMediaFile(file: File) {
        lifecycleScope.launch {
            try {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.file_provider",
                    file
                )
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/octet-stream")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intent)
            } catch (e: Exception) {
                errorHandler.exec(DataError.FileError(e)).apply {
                    messages.message(MessageParam(messageId, action = this.navAction))
                }
            }
        }
    }

    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = R.string.homework_assignments,
        menuRes = R.menu.menu_home_filter,
        onMenuItemClick = {
            when (it.itemId) {
                R.id.action_filter -> showFilterDialog()
            }
        }
    )
}