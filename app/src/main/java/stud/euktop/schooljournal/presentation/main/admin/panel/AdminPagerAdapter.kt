package stud.euktop.schooljournal.presentation.main.admin.panel

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdminPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdminEntityFragment.newInstance(EntityType.USERS)
            1 -> AdminEntityFragment.newInstance(EntityType.CLASSES)
            2 -> AdminEntityFragment.newInstance(EntityType.OBJECTS)
            3 -> AdminEntityFragment.newInstance(EntityType.APPOINTMENTS)
            else -> throw IllegalArgumentException()
        }
    }
}