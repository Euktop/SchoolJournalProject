package stud.euktop.schooljournal.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentMainMenuBinding
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMainMenu
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

    @Inject
    lateinit var router: RouterMainMenu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTeacherClasses.setOnClickListener { router.toMainMenuTeacherClasses() }
        binding.btnStudentSubjects.setOnClickListener { router.toMainMenuStudentSubjects() }
        binding.btnAdminPanel.setOnClickListener { router.toMainMenuAdminPanel() }
        binding.btnProfile.setOnClickListener { router.toMainMenuAuthProfile() }
        binding.btnStudentDetail.setOnClickListener { router.toStudentSubjectDetail() }
        binding.btnLogin.setOnClickListener { router.toNavAuth() }
        binding.btnProfileRegistration.setOnClickListener { router.toNavAuthWithProfile() }
        binding.btnCreatePassword.setOnClickListener { router.toNavAuthWithCreatePassword() }
        binding.btnSchools.setOnClickListener { router.toMainMenuAdminPanel() }
        binding.btnRooms.setOnClickListener { router.toMainMenuAdminPanel() }
        // Используем константу вместо магического числа
        binding.btnLessonMarks.setOnClickListener { router.toMainMenuLessonMarks(DEFAULT_LESSON_ID) }
        binding.btnHomework.setOnClickListener { router.toMainMenuTeacherHomeworkList() }
        binding.btnStudentHomework.setOnClickListener { router.toMainMenuStudentHomeworkList() }
        binding.btnLessonEdit.setOnClickListener { router.toMainMenuLessonEdit() }
        binding.btnStudentSchedule.setOnClickListener { router.toMainMenuStudentSchedule() }
        binding.btnSelectRole.setOnClickListener { router.toMainMenuSelectRole() }

        binding.btnHomeStudent.setOnClickListener { router.toMainMenuStudentHome() }
        binding.btnHomeAdmin.setOnClickListener { router.toMainMenuStudentAdmin() }
        binding.btnHomeTeacher.setOnClickListener { router.toMainMenuStudentTeacher() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DEFAULT_LESSON_ID = 0
    }
}