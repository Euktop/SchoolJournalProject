package stud.euktop.schooljournal.presentation.common.filter.assignment

import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.classes.AppClassInfoFilter
import stud.euktop.schooljournal.presentation.common.filter.classes.ClassFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.subject.SubjectFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.user.AppUserFilter
import stud.euktop.schooljournal.presentation.common.filter.user.UserFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder

@AndroidEntryPoint
class TeacherAssignmentFilterDialog(
    initialFilter: AppTeacherAssignmentFilter,
    onFilterApplied: (AppTeacherAssignmentFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<TeacherAssignmentFilterViewModel, AppTeacherAssignmentFilter>(
    initialFilter, onFilterApplied, onError
) {

    override val viewModel: TeacherAssignmentFilterViewModel by viewModels()

    private lateinit var teacherSelect: FilterFieldBuilder.AddSearchableSelectResult<UserProfile>
    private lateinit var classSelect: FilterFieldBuilder.AddSearchableSelectResult<ClassInfo>
    private lateinit var subjectSelect: FilterFieldBuilder.AddSearchableSelectResult<Subject>
    private lateinit var isPrimaryCheckBox: AppCompatCheckBox
    private lateinit var dateRange: FilterFieldBuilder.DateRangeResult

    override val setups: List<suspend () -> Unit> = listOf(
        { setupTeachers() },
        { setupClasses() },
        { setupSubjects() }
    )

    private suspend fun setupTeachers() {
        viewModel.teachers.collect { teachers ->
            teacherSelect.updateItems(teachers)
        }
    }

    private suspend fun setupClasses() {
        viewModel.classes.collect { classes ->
            classSelect.updateItems(classes)
        }
    }

    private suspend fun setupSubjects() {
        viewModel.subjects.collect { subjects ->
            subjectSelect.updateItems(subjects)
        }
    }

    override fun setupFilterFields(container: LinearLayout) {
        teacherSelect = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.teacher),
            items = emptyList(),
            toText = { it?.let { "${it.lastName} ${it.firstName}" } ?: "" },
            onSelected = { teacher ->
                initialFilter = initialFilter.copy(teacher = teacher)
            },
            initialSelectedItem = initialFilter.teacher,
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(UserFilterDialog.TAG) != null) return@addSearchableSelect
                val dialog = UserFilterDialog(
                    initialFilter = AppUserFilter(role = Role.TEACHER),
                    onFilterApplied = { userFilter ->
                        viewModel.loadTeachers(userFilter.toDomainFilter())
                    },
                    onError = this@TeacherAssignmentFilterDialog.onError
                )
                dialog.show(parentFragmentManager, UserFilterDialog.TAG)
            },
            onShowing = { viewModel.loadTeachers() }
        )

        classSelect = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.class_name),
            items = emptyList(),
            toText = { it?.let { "${it.grade}${it.letter}" } ?: "" },
            onSelected = { classInfo -> initialFilter = initialFilter.copy(classInfo = classInfo) },
            initialSelectedItem = initialFilter.classInfo,
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(ClassFilterDialog.TAG) != null) return@addSearchableSelect
                val dialog = ClassFilterDialog(
                    initialFilter = AppClassInfoFilter(),
                    onFilterApplied = { classFilter ->
                        viewModel.loadClasses(classFilter.toDomain())
                    },
                    onError = this@TeacherAssignmentFilterDialog.onError
                )
                dialog.show(parentFragmentManager, ClassFilterDialog.TAG)
            },
            onShowing = { viewModel.loadClasses() }
        )

        subjectSelect = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.subject),
            items = emptyList(),
            toText = { it?.name ?: "" },
            onSelected = { subject -> initialFilter = initialFilter.copy(subject = subject) },
            initialSelectedItem = initialFilter.subject,
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(SubjectFilterDialog.TAG) != null) return@addSearchableSelect
                val dialog = SubjectFilterDialog(
                    initialFilter = SubjectFilter(),
                    onFilterApplied = { subjectFilter -> viewModel.loadSubjects(subjectFilter) },
                    onError = this@TeacherAssignmentFilterDialog.onError
                )
                dialog.show(parentFragmentManager, SubjectFilterDialog.TAG)
            },
            onShowing = { viewModel.loadSubjects() }
        )

        // Чекбокс "Основной" вместо текстового поля
        isPrimaryCheckBox = AppCompatCheckBox(container.context).apply {
            text = getString(R.string.is_primary)
            isChecked = initialFilter.isPrimary == true
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 16 }
        }
        container.addView(isPrimaryCheckBox)

        dateRange = FilterFieldBuilder.addDateRange(
            parent = container,
            title = getString(R.string.valid_period),
            fromDate = initialFilter.validFrom,
            toDate = initialFilter.validTo,
            onFromDateSelected = { date -> initialFilter = initialFilter.copy(validFrom = date) },
            onToDateSelected = { date -> initialFilter = initialFilter.copy(validTo = date) }
        )
    }

    override fun resetFilters() {
        teacherSelect.select.state = teacherSelect.select.state.copy(selectText = "")
        classSelect.select.state = classSelect.select.state.copy(selectText = "")
        subjectSelect.select.state = subjectSelect.select.state.copy(selectText = "")
        isPrimaryCheckBox.isChecked = false
        dateRange.fromInput.state = dateRange.fromInput.state.copy(text = "")
        dateRange.toInput.state = dateRange.toInput.state.copy(text = "")
        initialFilter = AppTeacherAssignmentFilter()
    }

    override fun collectFilter(): AppTeacherAssignmentFilter {
        return initialFilter.copy(
            isPrimary = if (isPrimaryCheckBox.isChecked) true else null
        )
    }
}