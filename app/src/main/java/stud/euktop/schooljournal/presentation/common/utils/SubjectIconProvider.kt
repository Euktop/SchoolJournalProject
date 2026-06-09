package stud.euktop.schooljournal.presentation.common.utils

import androidx.annotation.DrawableRes
import stud.euktop.uikit.R

/**
 * Маппинг бизнес-сущности (предмета) на визуальное представление (иконку).
 *
 * Вся логика отображения — в UI-слое. Domain-модель остаётся чистой.
 * Маппинг идёт по [subjectId], так как имя предмета может измениться,
 * а ID — стабилен.
 */
object SubjectIconProvider {

    @DrawableRes
    fun getIcon(subjectId: Int): Int = when (subjectId) {
        1 -> R.drawable.ic_calculate //математика
        2 -> R.drawable.ic_menu_book //литература
        3 -> R.drawable.ic_rocket_launch //литература
        else -> R.drawable.ic_default_subject
    }
}