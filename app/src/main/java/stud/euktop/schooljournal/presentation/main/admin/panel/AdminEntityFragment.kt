package stud.euktop.schooljournal.presentation.main.admin.panel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.ClassInfo
import stud.euktop.domain.model.Subject
import stud.euktop.domain.model.TeacherAssignment
import stud.euktop.domain.model.UserInfo
import stud.euktop.schooljournal.databinding.FragmentAdminEntityBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
/**
 * Обобщённый фрагмент для отображения списка сущностей в админ-панели.
 *
 * Назначение: отображает одну из сущностей (Users, Classes, Subjects, TeacherAssignments)
 * в виде списка с возможностью редактирования и удаления.
 *
 * Роли: ADMIN, DIRECTOR
 *
 * Функционал:
 * - Получение типа сущности (EntityType) при создании фрагмента
 * - Использует shared ViewModel родительского AdminPanelFragment
 * - Отображает список через AdminAdapter с кнопками "Изменить" и "Удалить"
 * - При клике на "Изменить" открывается диалог/экран редактирования (пока TODO)
 * - При клике на "Удалить" – подтверждение и вызов репозитория (TODO после реализации CRUD-процедур)
 *
 * @param T тип сущности (UserInfo, ClassInfo, Subject, TeacherAssignment)
 * @see AdminPanelViewModel
 */
class AdminEntityFragment<out T : Any>(private val entityType: EntityType) : BaseFragment<
        FragmentAdminEntityBinding,
        AdminPanelViewModel,
        AdminPanelState,
        Unit>() {

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentAdminEntityBinding.inflate(i, c, false)

    override val viewModel: AdminPanelViewModel by viewModels({ requireParentFragment() })

    private val adapter: AdminAdapter<T> by lazy {
        when (entityType) {
            EntityType.USERS -> AdminAdapter(
                toText = { (it as UserInfo).run { "$lastName $firstName ($email)" } },
                onEditClick = { /* TODO */ },
                onDeleteClick = { /* TODO */ }
            )

            EntityType.CLASSES -> AdminAdapter(
                toText = { (it as ClassInfo).run { "$grade$letter - $schoolName" } },
                onEditClick = { /* TODO */ },
                onDeleteClick = { /* TODO */ }
            )

            EntityType.OBJECTS -> AdminAdapter(
                toText = { (it as Subject).name },
                onEditClick = { /* TODO */ },
                onDeleteClick = { /* TODO */ }
            )

            EntityType.APPOINTMENTS -> AdminAdapter(
                toText = { (it as TeacherAssignment).run { "$teacherName → $className · $subjectName" } },
                onEditClick = { /* TODO */ },
                onDeleteClick = { /* TODO */ }
            )
        }
    }

    override fun setupUI() {
        binding.rvEntity.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEntity.adapter = adapter
    }

    override fun updateState(state: AdminPanelState) {
        val list = when (entityType) {
            EntityType.USERS -> state.users
            EntityType.CLASSES -> state.classes
            EntityType.OBJECTS -> state.subjects
            EntityType.APPOINTMENTS -> state.assignments
        }
        adapter.submitList(list as List<T>)
    }

    override fun updateEvent(event: Unit) {}
}