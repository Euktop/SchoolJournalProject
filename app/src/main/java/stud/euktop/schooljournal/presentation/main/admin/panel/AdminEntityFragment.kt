package stud.euktop.schooljournal.presentation.main.admin.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.ClassInfo
import stud.euktop.domain.model.Subject
import stud.euktop.domain.model.TeacherAssignment
import stud.euktop.domain.model.UserInfo
import stud.euktop.schooljournal.R as R
import stud.euktop.uikit.R as R2
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.submitList
import stud.euktop.schooljournal.presentation.main.admin.classes.ClassEditViewModel
import stud.euktop.schooljournal.presentation.main.admin.subjects.SubjectEditViewModel
import stud.euktop.schooljournal.presentation.main.admin.users.UserEditViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AdminEntityFragment : BaseFragment<
        FragmentAdminEntityBinding,
        AdminPanelViewModel,
        AdminPanelState,
        Unit>() {

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var entityType: EntityType

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(i, c, false)

    override val viewModel: AdminPanelViewModel by viewModels({ requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entityType = arguments?.getSerializable(ARG_ENTITY_TYPE, EntityType::class.java)
            ?: throw IllegalArgumentException("EntityType must be provided")
    }

    override fun setupUI() {
        val adapter = when (entityType) {
            EntityType.USERS -> createUserAdapter()
            EntityType.CLASSES -> createClassAdapter()
            EntityType.OBJECTS -> createSubjectAdapter()
            EntityType.APPOINTMENTS -> createAssignmentAdapter()
        }
        binding.rvEntity.adapter = adapter
    }

    private fun createUserAdapter(): AdminAdapter<UserInfo> {
        return AdminAdapter(
            toText = { "${it.lastName} ${it.firstName} (${it.email})" },
            onEditClick = { user ->
                navigationManager.navigate(
                    NavCommand.ToDestination(
                        R.id.userEditFragment,
                        args = Bundle().apply {
                            putInt(UserEditViewModel.KEY_USER_ID, user.userId)
                        }
                    )
                )
            },
            onDeleteClick = { user ->
                showDeleteConfirmation(
                    title = getString(R.string.delete_user_confirmation, user.lastName)
                ) {
                    viewModel.deleteUser(user.userId)
                }
            }
        )
    }

    private fun createClassAdapter(): AdminAdapter<ClassInfo> {
        return AdminAdapter(
            toText = { "${it.grade}${it.letter} - ${it.schoolName}" },
            onEditClick = { classInfo ->
                navigationManager.navigate(
                    NavCommand.ToDestination(
                        R.id.classEditFragment,
                        args = Bundle().apply {
                            putInt(ClassEditViewModel.KEY_CLASS_ID, classInfo.classId)
                        }
                    )
                )
            },
            onDeleteClick = { classInfo ->
                showDeleteConfirmation(
                    getString(
                        R.string.delete_class_confirmation,
                        "${classInfo.grade}${classInfo.letter}"
                    )
                ) {
                    viewModel.deleteClass(classInfo.classId)
                }
            }
        )
    }

    // presentation/main/admin/panel/AdminEntityFragment.kt
    private fun createSubjectAdapter(): AdminAdapter<Subject> {
        return AdminAdapter(
            toText = { it.name },
            onEditClick = { subject ->
                navigationManager.navigate(
                    NavCommand.ToDestination(
                        R.id.subjectEditFragment,
                        args = Bundle().apply {
                            putInt(SubjectEditViewModel.KEY_SUBJECT_ID, subject.subjectId)
                        }
                    )
                )
            },
            onDeleteClick = { subject ->
                showDeleteConfirmation(
                    getString(R.string.delete_subject_confirmation, subject.name)
                ) {
                    viewModel.deleteSubject(subject.subjectId)
                }
            }
        )
    }

    private fun createAssignmentAdapter(): AdminAdapter<TeacherAssignment> {
        return AdminAdapter(
            toText = { "${it.teacherName} → ${it.className} · ${it.subjectName}" },
            onEditClick = { /* TODO */ },
            onDeleteClick = { /* TODO */ }
        )
    }

    private fun showDeleteConfirmation(title: String, onConfirm: () -> Unit) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_confirmation)
            .setMessage(title)
            .setPositiveButton(R.string.delete) { _, _ -> onConfirm() }
            .setNegativeButton(R2.string.cancel, null)
            .show()
    }

    override fun updateState(state: AdminPanelState) {
        val list = when (entityType) {
            EntityType.USERS -> state.users
            EntityType.CLASSES -> state.classes
            EntityType.OBJECTS -> state.subjects
            EntityType.APPOINTMENTS -> state.assignments
        }
        binding.rvEntity.submitList(list)
    }

    override fun updateEvent(event: Unit) {}

    companion object {
        private const val ARG_ENTITY_TYPE = "entityType"

        fun newInstance(entityType: EntityType): AdminEntityFragment {
            return AdminEntityFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ENTITY_TYPE, entityType)
                }
            }
        }
    }
}