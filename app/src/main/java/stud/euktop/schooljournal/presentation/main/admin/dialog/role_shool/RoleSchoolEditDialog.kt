package stud.euktop.schooljournal.presentation.main.admin.dialog.role_shool

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.Role
import stud.euktop.schooljournal.presentation.common.filter.school.SchoolFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.RepositoryExec
import stud.euktop.uikit.R
import stud.euktop.schooljournal.presentation.common.utils.toMessageId
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.filter.FilterFieldBuilder.AddSearchableSelectResult
import stud.euktop.uikit.components.input.select.ListSafe

@AndroidEntryPoint
class RoleSchoolEditDialog : DialogFragment(), RepositoryExec {
    private val viewModel: RoleSchoolEditViewModel by viewModels()

    private lateinit var roleSelect: FilterFieldBuilder.SelectResult<Role>
    private lateinit var schoolSelect: AddSearchableSelectResult<School>
    private var schoolFilter: SchoolFilter = SchoolFilter()
    var selectedRole: Role? = null
    var selectedSchool: School? = null
    var onSuccess: ((Role, School?) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 30)
        }


        val roleListSafe = ListSafe<Role>(
            toText = { it?.let { requireContext().getString(it.toMessageId()) } ?: "" },
            onClick = { role, _ ->
                selectedRole = role
                if (role == Role.ADMIN) schoolSelect.select.visibility = View.GONE
            })
        roleSelect = FilterFieldBuilder.addSingleSelect(
            parent = binding,
            fragmentManager = childFragmentManager,
            title = getString(R.string.role),
            items = roleListSafe,
            selectedItem = arguments?.getSerializable(ARG_ROLE, Role::class.java),
            onShowing = { viewModel.loadRole() }
        )

        val schoolListSafe = ListSafe(
            values = viewModel.schools.value,
            toText = { it?.name ?: "" },
            onClick = { school, _ -> selectedSchool = school })
        schoolSelect = FilterFieldBuilder.addSearchableSelect(
            parent = binding,
            fragmentManager = childFragmentManager,
            title = getString(R.string.school),
            items = schoolListSafe,
            onShowing = { viewModel.loadSchools(schoolFilter) },
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(SchoolFilterDialog.TAG) != null) return@addSearchableSelect
                SchoolFilterDialog(
                    schoolFilter, {
                        schoolFilter = it
                        viewModel.loadSchools(schoolFilter)
                    }, onError
                ).show(parentFragmentManager, SchoolFilterDialog.TAG)
            }).apply {
            select.apply {
                state = state.copy(selectText = arguments?.getString(ARG_SCHOOL_NAME))
            }
        }


        // Подписываемся на обновления школ
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.schools.collect { schools ->
                    schoolSelect.register.updateItems(schools)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.role.collect { roles ->
                    roleSelect.register.updateItems(roles)
                }
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.edit_role_school)
            .setView(binding)
            .setPositiveButton(R.string.save) { _, _ ->
                selectedSchool = if (selectedRole == Role.ADMIN) null else selectedSchool
                selectedRole?.let { onSuccess?.invoke(it, selectedSchool) }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    override var onError: (CoordinatorResult.Error) -> Unit
        get() = viewModel.onError
        set(value) {
            viewModel.onError = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (onErrorInit != null) onError = onErrorInit ?: {}
    }

    private var onErrorInit: ((CoordinatorResult.Error) -> Unit)? = null

    companion object {
        private const val ARG_ROLE = "role"
        private const val ARG_SCHOOL_ID = "schoolId"
        private const val ARG_SCHOOL_NAME = "schoolName"
        const val TAG = "RoleSchoolEditDialog"

        fun newInstance(
            role: Role? = null,
            school: School? = null,
            onError: RepositoryExec,
            onSuccess: ((Role, School?) -> Unit)
        ): RoleSchoolEditDialog {
            return RoleSchoolEditDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ROLE, role)
                    putInt(ARG_SCHOOL_ID, school?.schoolId ?: -1)
                    putString(ARG_SCHOOL_NAME, school?.name)
                }
                this.onErrorInit = onError.onError
                this.onSuccess = onSuccess
            }
        }
    }
}