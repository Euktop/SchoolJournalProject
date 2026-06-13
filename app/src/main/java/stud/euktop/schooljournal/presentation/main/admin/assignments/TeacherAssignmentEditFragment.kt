package stud.euktop.schooljournal.presentation.main.admin.assignments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherAssignmentEditBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.binding.bindDate
import stud.euktop.schooljournal.presentation.common.binding.bindPagingSelect
import stud.euktop.schooljournal.presentation.common.binding.toInit
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import java.text.SimpleDateFormat
import java.util.Locale
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

@AndroidEntryPoint
class TeacherAssignmentEditFragment :
    BaseFragment<
            FragmentTeacherAssignmentEditBinding,
            TeacherAssignmentEditViewModel,
            TeacherAssignmentEditState,
            Unit
            >() {

    override val viewModel: TeacherAssignmentEditViewModel by viewModels()
    private lateinit var loadingDelegate: LoadingDelegate<TeacherAssignmentEditState>
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherAssignmentEditBinding.inflate(inflater, container, false)

    override fun setupUI() {
        loadingDelegate = LoadingDelegate(viewModel, viewLifecycleOwner)

        // Учитель
        bindPagingSelect(
            select = binding.selectTeacher,
            viewModel = viewModel,
            title = getString(R.string.teacher),
            filterFlow = viewModel.teacherFilterFlow,
            getPagingDataFlow = viewModel::getTeachersPagingDataFlow,
            getSelectedValue = { it.teacher },
            toText = { it?.let { "${it.lastName} ${it.firstName}" } ?: "" },
            onSelected = viewModel::updateTeacher,
            onFilterClick = { /* можно добавить фильтр учителей */ },
            fragmentManager = childFragmentManager,
            lifecycleOwner = viewLifecycleOwner
        )

        // Класс
        bindPagingSelect(
            select = binding.selectClass,
            viewModel = viewModel,
            title = getString(R.string.class_name),
            filterFlow = viewModel.classFilterFlow,
            getPagingDataFlow = viewModel::getClassesPagingDataFlow,
            getSelectedValue = { it.classInfo },
            toText = { it?.let { "${it.grade}${it.letter}" } ?: "" },
            onSelected = viewModel::updateClass,
            onFilterClick = { /* фильтр классов */ },
            fragmentManager = childFragmentManager,
            lifecycleOwner = viewLifecycleOwner
        )

        // Предмет
        bindPagingSelect(
            select = binding.selectSubject,
            viewModel = viewModel,
            title = getString(R.string.subject),
            filterFlow = viewModel.subjectFilterFlow,
            getPagingDataFlow = viewModel::getSubjectsPagingDataFlow,
            getSelectedValue = { it.subject },
            toText = { it?.name ?: "" },
            onSelected = viewModel::updateSubject,
            onFilterClick = { /* фильтр предметов */ },
            fragmentManager = childFragmentManager,
            lifecycleOwner = viewLifecycleOwner
        )

        // Дата начала
        bindDate(
            input = binding.inputValidFrom,
            viewModel = viewModel,
            getter = { it.validFrom },
            setter = viewModel::updateValidFrom,
            format = dateFormat
        )

        // Дата окончания
        bindDate(
            input = binding.inputValidTo,
            viewModel = viewModel,
            getter = { it.validTo },
            setter = viewModel::updateValidTo,
            format = dateFormat
        )

        // Чекбокс "Основной"
        binding.chkIsPrimary.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateIsPrimary(isChecked)
        }

        // Кнопки Сохранить/Отмена
        binding.buttonsSaveCancel.toInit(loadingDelegate, viewModel::save, viewModel::cancel)
    }

    override fun updateState(state: TeacherAssignmentEditState) {
        try {
            logger?.d(this.toSimpleTag(), "updateState", "teacher=${state.teacher?.let { "${it.lastName} ${it.firstName}" }}, class=${state.classInfo?.let { "${it.grade}${it.letter}" }}, subject=${state.subject?.name}")
        } catch (_: Throwable) {
        }
        binding.selectTeacher.state = binding.selectTeacher.state.copy(
            selectText = state.teacher?.let { "${it.lastName} ${it.firstName}" } ?: ""
        )
        binding.selectClass.state = binding.selectClass.state.copy(
            selectText = state.classInfo?.let { "${it.grade}${it.letter}" } ?: ""
        )
        binding.selectSubject.state = binding.selectSubject.state.copy(
            selectText = state.subject?.name ?: ""
        )
        binding.inputValidFrom.state = binding.inputValidFrom.state.copy(
            text = state.validFrom?.let { dateFormat.format(it) } ?: ""
        )
        binding.inputValidTo.state = binding.inputValidTo.state.copy(
            text = state.validTo?.let { dateFormat.format(it) } ?: ""
        )
        binding.chkIsPrimary.isChecked = state.isPrimary
        binding.buttonsSaveCancel.btnSave.isEnabled = state.isFormValid()
    }

    override fun updateEvent(event: Unit) { /* не используется */ }
}