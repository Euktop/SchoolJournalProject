package stud.euktop.schooljournal.presentation.common.filter.classes

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.school.SchoolFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput

@AndroidEntryPoint
class ClassFilterDialog(
    initialFilter: AppClassInfoFilter,
    onFilterApplied: (AppClassInfoFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<ClassFilterViewModel, AppClassInfoFilter>(
    initialFilter, onFilterApplied, onError
) {

    companion object {
        const val TAG = "ClassFilterDialog"
    }

    override val viewModel: ClassFilterViewModel by viewModels()

    private lateinit var schoolSelectResult: FilterFieldBuilder.AddSearchableSelectResult<School>
    private lateinit var classSearchInput: SchJInput
    private lateinit var teacherSelectResult: FilterFieldBuilder.AddSearchableSelectResult<UserListItem>
    private lateinit var yearStartInput: SchJInput
    private lateinit var yearEndInput: SchJInput

    override val setups: List<suspend () -> Unit> = listOf(
        { setupSchools() },
        { setupTeachers() }
    )

    private suspend fun setupSchools() {
        viewModel.schools.collect { schools ->
            schoolSelectResult.updateItems(schools)
        }
    }

    private suspend fun setupTeachers() {
        viewModel.teachers.collect { teachers ->
            teacherSelectResult.updateItems(teachers)
        }
    }

    override fun setupFilterFields(container: LinearLayout) {
        // Выбор школы (исправленный вызов addSearchableSelect)
        schoolSelectResult = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.school),
            items = emptyList(),  // начальный пустой список, обновится через updateItems
            toText = { it?.name ?: "" },
            onSelected = { school ->
                initialFilter = initialFilter.copy(school = school)
            },
            initialSelectedItem = initialFilter.school,
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(SchoolFilterDialog.TAG) != null) return@addSearchableSelect
                val dialog = SchoolFilterDialog(
                    initialFilter = initialFilter.schoolFilter ?: SchoolFilter(),
                    onFilterApplied = { schoolFilter ->
                        initialFilter = initialFilter.copy(schoolFilter = schoolFilter)
                        viewModel.loadSchools(schoolFilter)
                    },
                    onError = this.onError
                )
                dialog.show(parentFragmentManager, SchoolFilterDialog.TAG)
            },
            onShowing = { viewModel.loadSchools(initialFilter.schoolFilter ?: SchoolFilter()) }
        )

        // Поиск класса (строка, например "5А")
        classSearchInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(R.string.class_name_hint),
            initialValue = initialFilter.query ?: ""
        )

        // Выбор учителя (исправленный вызов addSearchableSelect)
        teacherSelectResult = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.class_teacher),
            items = emptyList(),
            toText = { user -> user?.let { "${it.lastName} ${it.firstName}" } ?: "" },
            onSelected = { teacher ->
                initialFilter = initialFilter.copy(teacher = teacher)
            },
            initialSelectedItem = initialFilter.teacher,
            onShowing = { viewModel.loadTeachers(initialFilter.teacherFilter ?: UserFilter()) }
        )

        // Год начала
        yearStartInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(R.string.academic_year_start),
            initialValue = initialFilter.academicYearStart?.toString() ?: ""
        )

        // Год окончания
        yearEndInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(R.string.academic_year_end),
            initialValue = initialFilter.academicYearEnd?.toString() ?: ""
        )
    }

    override fun resetFilters() {
        schoolSelectResult.select.state = schoolSelectResult.select.state.copy(selectText = "")
        classSearchInput.state = classSearchInput.state.copy(text = "")
        teacherSelectResult.select.state = teacherSelectResult.select.state.copy(selectText = "")
        yearStartInput.state = yearStartInput.state.copy(text = "")
        yearEndInput.state = yearEndInput.state.copy(text = "")
        initialFilter = AppClassInfoFilter()
    }

    override fun collectFilter(): AppClassInfoFilter {
        return initialFilter.copy(
            query = classSearchInput.state.text.takeIf { it.isNotBlank() },
            academicYearStart = yearStartInput.state.text.toIntOrNull(),
            academicYearEnd = yearEndInput.state.text.toIntOrNull()
        )
    }
}