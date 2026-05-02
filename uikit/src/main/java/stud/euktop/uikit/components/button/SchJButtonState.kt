package stud.euktop.uikit.components.button

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import stud.euktop.uikit.R

data class SchJButtonState(val buttonType: ButtonType, val buttonClass: ButtonClass) {
    enum class ButtonType {
        BIG,
        SMALL,
        CHIPS
    }

    enum class ButtonClass {
        PRIMARY,
        INACTIVE,
        SECONDARY,
        TETRIARY,
        SELECT,
        UNSELECT;

        fun updateSelect(isSelect: Boolean): ButtonClass {
            return if (isSelect) SELECT
            else UNSELECT
        }

        fun isSelect(): Boolean {
            return this == SELECT
        }
    }

    fun updateView(view: View) {
        val back = when (this.buttonClass) {
            ButtonClass.PRIMARY, ButtonClass.SELECT -> R.color.color_text_accent
            ButtonClass.INACTIVE -> R.color.color_accent_inactive
            ButtonClass.SECONDARY -> R.color.transparent
            ButtonClass.UNSELECT, ButtonClass.TETRIARY -> R.color.color_bg_secondary
        }
        val stroke = when (this.buttonClass) {
            ButtonClass.SECONDARY -> R.color.color_accent
            else -> R.color.transparent
        }

        val vMargin = view.resources.getDimension(
            when (this.buttonType) {
                ButtonType.BIG -> R.dimen.big_button_padding
                ButtonType.SMALL -> R.dimen.small_button_vertical_padding
                ButtonType.CHIPS -> R.dimen.chip_button_vertical_padding
            }
        ).toInt()
        val hMargin = view.resources.getDimension(
            when (this.buttonType) {
                ButtonType.BIG -> R.dimen.big_button_padding
                ButtonType.SMALL -> R.dimen.small_button_horizontal_padding
                ButtonType.CHIPS -> R.dimen.chip_button_horizontal_padding
            }
        ).toInt()

        view.apply {
            background = GradientDrawable().apply {
                cornerRadius = resources.getDimension(R.dimen.corner_base)
                setColor(ContextCompat.getColor(context, back))
                setStroke(
                    resources.getDimension(R.dimen.stroke_width_base).toInt(),
                    ContextCompat.getColor(context, stroke)
                )
            }
            setPadding(hMargin, vMargin, hMargin, vMargin)
        }
    }

    fun updateView(view: TextView) {
        updateView(view as View)
        view.setTextAppearance(
            when (buttonType) {
                ButtonType.BIG -> R.style.TextAppearance_SchJ_Title3_SemiBold
                ButtonType.SMALL -> R.style.TextAppearance_SchJ_Caption_SemiBold
                ButtonType.CHIPS -> R.style.TextAppearance_SchJ_Text_Medium
            }
        )
        view.setTextColor(
            ContextCompat.getColor(
                view.context, when (buttonClass) {
                    ButtonClass.PRIMARY, ButtonClass.INACTIVE, ButtonClass.SELECT -> R.color.color_text_on_accent
                    ButtonClass.SECONDARY -> R.color.color_text_accent
                    ButtonClass.TETRIARY -> R.color.color_text_primary
                    ButtonClass.UNSELECT -> R.color.color_text_tertiary
                }
            )
        )
    }
}