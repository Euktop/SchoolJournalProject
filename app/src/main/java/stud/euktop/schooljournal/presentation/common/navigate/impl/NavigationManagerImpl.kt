package stud.euktop.schooljournal.presentation.common.navigate.impl

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.schooljournal.presentation.common.navigate.Destination
import stud.euktop.schooljournal.presentation.common.navigate.Group
import stud.euktop.schooljournal.presentation.common.navigate.StackAction
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.mainDestination
import stud.euktop.uikit.R
import javax.inject.Inject

class NavigationManagerImpl @Inject constructor(
    private val fragmentManager: FragmentManager,
    @param:IdRes private val containerId: Int
) : NavigationManager {
    private var lastGroup: Group? = null
    override fun navigateTo(
        destination: Destination,
        stackAction: StackAction
    ) {
        val fragmentTag = "${destination.group.name}_${destination.id}"
        val fragment = destination.action()

        var transaction: FragmentTransaction? = null
        fun transact() {
            transaction = fragmentManager.beginTransaction().apply { applyDefaultAnimations(this) }
            transaction.replace(containerId, fragment, fragmentTag)
        }
        when (stackAction) {
            StackAction.KEEP -> {
                transact()
                transaction?.addToBackStack(fragmentTag)
            }

            StackAction.REPLACE -> {
                transact()
            }

            StackAction.CLEAR_ALL -> {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transact()
            }

            StackAction.CLEAR_GROUP -> {
                clearGroupFromBackStack(lastGroup)
                transact()
            }
        }
        lastGroup = destination.group
        logger?.d(
            this.toSimpleTag(),
            "Navigate",
            "To ${fragment.toSimpleTag()}; id: ${destination.id}; group: ${destination.group.name} ; Action: ${stackAction.name}"
        )
        transaction?.commit()
    }

    override fun navigateTo(
        group: Group,
        stackAction: StackAction
    ) = navigateTo(mainDestination.getValue(group), stackAction)

    override fun switchTab(
        destination: Destination,
        clearHistory: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun back() {
        TODO("Not yet implemented")
    }

    override fun backGroup() {
        TODO("Not yet implemented")
    }

    override fun back(destination: Destination) {
        TODO("Not yet implemented")
    }

    override fun backGroup(group: Group) {
        TODO("Not yet implemented")
    }


    private fun applyDefaultAnimations(transaction: FragmentTransaction) {
        transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right,
        )
    }

    private fun clearGroupFromBackStack(group: Group?) {
        if (group == null) return
        val backStackCount = fragmentManager.backStackEntryCount
        var firstGroupEntryId: Int? = null
        for (i in backStackCount - 1 downTo 0) {
            val entry = fragmentManager.getBackStackEntryAt(i)
            if (entry.name?.startsWith("${group.name}_") == true) {
                firstGroupEntryId = entry.id
            } else {
                break
            }
        }
        firstGroupEntryId?.let { id ->
            fragmentManager.popBackStackImmediate(id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onBackPressed(): Boolean {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
            return true
        }
        return false
    }
}