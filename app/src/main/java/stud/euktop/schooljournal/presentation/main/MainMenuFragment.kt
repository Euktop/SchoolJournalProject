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

        binding.btnTeacherClasses.setOnClickListener { router.toTeacherClasses() }
        binding.btnStudentSubjects.setOnClickListener { router.toStudentSubjects() }
        binding.btnAdminPanel.setOnClickListener { router.toAdminPanel() }
        binding.btnProfile.setOnClickListener { router.toAuthProfile() }
        binding.btnStudentDetail.setOnClickListener { router.toStudentSubjectDetail() }
        binding.btnLogin.setOnClickListener { router.toNavAuth() }
        binding.btnProfileRegistration.setOnClickListener { router.toNavAuthWithProfile() }
        binding.btnCreatePassword.setOnClickListener { router.toNavAuthWithCreatePassword() }
        binding.btnSchools.setOnClickListener { router.toAdminPanel() }
        binding.btnRooms.setOnClickListener { router.toAdminPanel() }
        binding.btnLessonMarks.setOnClickListener { router.toLessonMarks(101) }
        binding.btnHomework.setOnClickListener { router.toTeacherHomeworkList() }
        binding.btnStudentHomework.setOnClickListener { router.toStudentHomeworkList() }
        binding.btnLessonEdit.setOnClickListener { router.toLessonEdit() }
        binding.btnStudentSchedule.setOnClickListener { router.toStudentSchedule() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}