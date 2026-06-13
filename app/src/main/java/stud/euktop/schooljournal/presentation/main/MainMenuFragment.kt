package stud.euktop.schooljournal.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.loger.logger
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

        binding.btnTeacherClasses.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuTeacherClasses")
            } catch (_: Throwable) {
            }
            router.toMainMenuTeacherClasses() 
        }
        binding.btnStudentSubjects.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuStudentSubjects")
            } catch (_: Throwable) {
            }
            router.toMainMenuStudentSubjects() 
        }
        binding.btnAdminPanel.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuAdminPanel")
            } catch (_: Throwable) {
            }
            router.toMainMenuAdminPanel() 
        }
        binding.btnProfile.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuAuthProfile")
            } catch (_: Throwable) {
            }
            router.toMainMenuAuthProfile() 
        }
        binding.btnStudentDetail.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toStudentSubjectDetail")
            } catch (_: Throwable) {
            }
            router.toStudentSubjectDetail() 
        }
        binding.btnLogin.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toNavAuth")
            } catch (_: Throwable) {
            }
            router.toNavAuth() 
        }
        binding.btnProfileRegistration.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toNavAuthWithProfile")
            } catch (_: Throwable) {
            }
            router.toNavAuthWithProfile() 
        }
        binding.btnCreatePassword.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toNavAuthWithCreatePassword")
            } catch (_: Throwable) {
            }
            router.toNavAuthWithCreatePassword() 
        }
        binding.btnSchools.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuAdminPanel")
            } catch (_: Throwable) {
            }
            router.toMainMenuAdminPanel() 
        }
        binding.btnRooms.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuAdminPanel")
            } catch (_: Throwable) {
            }
            router.toMainMenuAdminPanel() 
        }
        // Используем константу вместо магического числа
        binding.btnLessonMarks.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuLessonMarks")
            } catch (_: Throwable) {
            }
            router.toMainMenuLessonMarks(DEFAULT_LESSON_ID) 
        }
        binding.btnHomework.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuTeacherHomeworkList")
            } catch (_: Throwable) {
            }
            router.toMainMenuTeacherHomeworkList() 
        }
        binding.btnStudentHomework.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuStudentHomeworkList")
            } catch (_: Throwable) {
            }
            router.toMainMenuStudentHomeworkList() 
        }
        binding.btnLessonEdit.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuLessonEdit")
            } catch (_: Throwable) {
            }
            router.toMainMenuLessonEdit() 
        }
        binding.btnStudentSchedule.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuStudentSchedule")
            } catch (_: Throwable) {
            }
            router.toMainMenuStudentSchedule() 
        }
        binding.btnSelectRole.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuSelectRole")
            } catch (_: Throwable) {
            }
            router.toMainMenuSelectRole() 
        }

        binding.btnHomeStudent.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuStudentHome")
            } catch (_: Throwable) {
            }
            router.toMainMenuStudentHome() 
        }
        binding.btnHomeAdmin.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuStudentAdmin")
            } catch (_: Throwable) {
            }
            router.toMainMenuStudentAdmin() 
        }
        binding.btnHomeTeacher.setOnClickListener { 
            try {
                logger?.d(this::class.java.simpleName, "navigate", "toMainMenuStudentTeacher")
            } catch (_: Throwable) {
            }
            router.toMainMenuStudentTeacher() 
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DEFAULT_LESSON_ID = 0
    }
}