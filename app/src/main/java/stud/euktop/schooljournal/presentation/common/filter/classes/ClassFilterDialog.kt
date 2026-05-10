// presentation/common/filter/class/ClassFilterDialog.kt
package stud.euktop.schooljournal.presentation.common.filter.classes

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.school.SchoolFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput
import stud.euktop.uikit.components.input.select.ListSafe

@AndroidEntryPoint
class ClassFilterDialog(
    initialFilter: ClassInfoFilter,
    onFilterApplied: (ClassInfoFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<ClassFilterViewModel, ClassInfoFilter>(
    initialFilter, onFilterApplied, onError
) {

    companion object {
        const val TAG = "ClassFilterDialog"
    }

    override val viewModel: ClassFilterViewModel by viewModels()

    private lateinit var schoolSelectResult: FilterFieldBuilder.AddSearchableSelectResult<School>
    private lateinit var classSearchInput: SchJInput
    private lateinit var teacherSelectResult: FilterFieldBuilder.AddSearchableSelectResult<UserProfile>
    private lateinit var yearStartInput: SchJInput
    private lateinit var yearEndInput: SchJInput

    override val setups: List<suspend () -> Unit> = listOf(
        { setupSchools() },
        { setupTeachers() }
    )

    private suspend fun setupSchools() {
        viewModel.schools.collect { schools ->
            schoolSelectResult.register.updateItems(schools)
        }
    }

    private suspend fun setupTeachers() {
        viewModel.teachers.collect { teachers ->
            teacherSelectResult.register.updateItems(teachers)
        }
    }

    override fun setupFilterFields(container: LinearLayout) {
        // Выбор школы
        schoolSelectResult = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.school),
            items = ListSafe(
                toText = { it?.name ?: "" },
                onClick = { school, _ -> initialFilter = initialFilter.copy(school = school) }
            ),
            initialSelectedItem = initialFilter.school,
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(SchoolFilterDialog.TAG) != null) return@addSearchableSelect
                val dialog = SchoolFilterDialog(
                    initialFilter = initialFilter.schoolFilter,
                    onFilterApplied = { schoolFilter ->
                        initialFilter = initialFilter.copy(schoolFilter = schoolFilter)
                        viewModel.loadSchools(schoolFilter)
                    },
                    onError = this.onError
                )
                dialog.show(parentFragmentManager, SchoolFilterDialog.TAG)
            },
            onShowing = { viewModel.loadSchools(initialFilter.schoolFilter) }
        )

        // Поиск класса (строка, например "5А")
        classSearchInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(R.string.class_name_hint),
            initialValue = initialFilter.query ?: ""
        )

        // Выбор учителя
        teacherSelectResult = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.class_teacher),
            items = ListSafe(
                toText = { it?.fullName ?: "" },
                onClick = { teacher, _ -> initialFilter = initialFilter.copy(teacher = teacher) }
            ),
            initialSelectedItem = initialFilter.teacher,
            onShowing = { viewModel.loadTeachers(initialFilter.teacherFilter) }
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
        initialFilter = ClassInfoFilter()
    }

    override fun collectFilter(): ClassInfoFilter {
        return initialFilter.copy(
            query = classSearchInput.state.text.takeIf { it.isNotBlank() },
            academicYearStart = yearStartInput.state.text.toIntOrNull(),
            academicYearEnd = yearEndInput.state.text.toIntOrNull()
        )
    }
}