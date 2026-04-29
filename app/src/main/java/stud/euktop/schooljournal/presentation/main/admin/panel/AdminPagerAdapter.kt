package stud.euktop.schooljournal.presentation.main.admin.panel

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import stud.euktop.domain.model.ClassInfo
import stud.euktop.domain.model.Subject
import stud.euktop.domain.model.TeacherAssignment
import stud.euktop.domain.model.UserInfo

class AdminPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AdminEntityFragment<UserInfo>(EntityType.USERS)
            }
            1 -> AdminEntityFragment<ClassInfo>(EntityType.CLASSES)
            2 -> AdminEntityFragment<Subject>(EntityType.OBJECTS)
            3 -> AdminEntityFragment<TeacherAssignment>(EntityType.APPOINTMENTS)
            else -> throw IllegalArgumentException()
        }
    }
}