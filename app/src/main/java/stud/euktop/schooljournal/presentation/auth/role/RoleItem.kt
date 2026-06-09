package stud.euktop.schooljournal.presentation.auth.role

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import stud.euktop.domain.model.user.Role
import stud.euktop.uikit.R

data class RoleItem(
    val role: Role,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int,
    @param:DrawableRes val iconRes: Int,
    val isSelected: Boolean = false
) {
    companion object {
        fun Role.toItem() =
            when (this) {
                Role.ADMIN -> RoleItem(
                    role = this,
                    titleRes = R.string.role_admin,
                    descriptionRes = R.string.role_admin_desc,
                    iconRes = R.drawable.ic_admin,
                )

                Role.DIRECTOR -> RoleItem(
                    role = this,
                    titleRes = R.string.role_director,
                    descriptionRes = R.string.role_director_desc,
                    iconRes = R.drawable.ic_school_director,
                )

                Role.TEACHER -> RoleItem(
                    role = this,
                    titleRes = R.string.role_teacher,
                    descriptionRes = R.string.role_teacher_desc,
                    iconRes = R.drawable.ic_school,
                )

                Role.STUDENT -> RoleItem(
                    role = this,
                    titleRes = R.string.role_student,
                    descriptionRes = R.string.role_student_desc,
                    iconRes = R.drawable.ic_schoolboy,
                )

                Role.PARENT -> RoleItem(
                    role = this,
                    titleRes = R.string.role_parent,
                    descriptionRes = R.string.role_parent_desc,
                    iconRes = R.drawable.ic_parents,
                )
            }
    }
}