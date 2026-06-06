package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherClassesBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import stud.euktop.schooljournal.presentation.common.utils.submitList
import stud.euktop.schooljournal.presentation.main.teacher.teacherLessons.TeacherLessonsViewModel
import javax.inject.Inject

/**
 * Экран списка классов и предметов для учителя.
 *
 * Назначение: отображает все пары (класс, предмет), которые учитель ведёт
 * в текущем учебном году (на основе TeacherClassSubjects).
 *
 * Роли: TEACHER
 *
 * Функционал:
 * - Загрузка списка через TeacherRepository.getTeacherClasses()
 * - Отображение элементов в RecyclerView (MaterialCardView с названием класса и предмета)
 * - При клике на элемент переход к экрану TeacherLessonsFragment
 *   с передачей classId и subjectId через аргументы навигации
 * - Обработка пустого состояния и ошибок загрузки
 *
 * @see TeacherClassesViewModel
 */
@AndroidEntryPoint
class TeacherClassesFragment : BaseFragment<
        FragmentTeacherClassesBinding,
        TeacherClassesViewModel,
        TeacherClassesState,
        Unit
        >() {
    @Inject
    internal lateinit var navigationManager: NavigationManager
    @Inject internal lateinit var router: RouterTeacher

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherClassesBinding.inflate(i, c, false)

    override val viewModel: TeacherClassesViewModel by viewModels()

    override fun setupUI() {
        binding.rvClasses.adapter = TeacherClassAdapter { item ->
            router.toTeacherLessons(item.classId, item.subjectId)
        }
        viewModel.loadClasses()
    }

    override fun updateState(state: TeacherClassesState) {
        binding.rvClasses.submitList(state.classes)
    }

    override fun updateEvent(event: Unit) {}
}