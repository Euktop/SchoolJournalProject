package stud.euktop.schooljournal.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentMainMenuBinding
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.main.teacher.lessonMarks.LessonMarksViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTeacherClasses.setOnClickListener {
            navigationManager.navigate(
                NavCommand.ToDestination(
                    R.id.teacherClassesFragment
                )
            )
        }
        binding.btnStudentSubjects.setOnClickListener {
            navigationManager.navigate(
                NavCommand.ToDestination(
                    R.id.studentSubjectsFragment
                )
            )
        }
        binding.btnAdminPanel.setOnClickListener {
            navigationManager.navigate(
                NavCommand.ToDestination(
                    R.id.adminPanelFragment
                )
            )
        }
        binding.btnProfile.setOnClickListener {
            navigationManager.navigate(
                NavCommand.ToDestination(
                    R.id.profileFragment
                )
            )
        }
        binding.btnStudentDetail.setOnClickListener {
            navigationManager.navigate(
                NavCommand.ToDestination(
                    R.id.studentSubjectDetailFragment
                )
            )
        }
        binding.btnLogin.setOnClickListener {
            navigationManager.navigate(
                NavCommand.ToDestination(R.id.nav_auth),
                NavCommand.ToDestination(R.id.loginFragment)
            )
        }
        binding.btnProfileRegistration.setOnClickListener {
            navigationManager.navigate(
                NavCommand.ToDestination(R.id.nav_auth),
                NavCommand.ToDestination(R.id.profileFragment)
            )
        }
        binding.btnCreatePassword.setOnClickListener {
            navigationManager.navigate(
                NavCommand.ToDestination(R.id.nav_auth),
                NavCommand.ToDestination(R.id.createPasswordFragment)
            )
        }
        binding.btnSchools.setOnClickListener {
            // Пока заглушка – переход на AdminPanel (вкладка 2) или отдельный фрагмент, если создашь
            navigationManager.navigate(NavCommand.ToDestination(R.id.adminPanelFragment))
        }
        binding.btnRooms.setOnClickListener {
            // Аналогично, в будущем можно сделать отдельный список
            navigationManager.navigate(NavCommand.ToDestination(R.id.adminPanelFragment))
        }
        binding.btnLessonMarks.setOnClickListener {
            // Откроем LessonMarksFragment с тестовым lessonId (например, 101)
            navigationManager.navigate(
                NavCommand.ToDestination(
                    R.id.lessonMarksFragment,
                    args = Bundle().apply { putInt(LessonMarksViewModel.LESSON_ID_KEY, 101) }
                ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}