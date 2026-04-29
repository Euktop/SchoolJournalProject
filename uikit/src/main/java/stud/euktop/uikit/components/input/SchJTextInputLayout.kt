package stud.euktop.uikit.components.input

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import stud.euktop.uikit.R

class SchJTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.textInputStyle,
) : TextInputLayout(context, attrs, defStyleAttr) {
    init {
        post {
            val f = editText?.onFocusChangeListener
            editText?.onFocusChangeListener =
                OnFocusChangeListener { v, h ->
                    f?.onFocusChange(v, h)
                    updateStrokeAndBackground(v.isFocused, hasError())
                }
        }
    }

    var hasError: () -> Boolean = { false }
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun updateStrokeAndBackground(hasFocus: Boolean, hasError: Boolean) {
        val strokeColorRes = when {
            hasError -> R.color.color_error
            hasFocus -> R.color.color_accent
            else -> R.color.color_stroke_default
        }
        val bgColorRes = when {
            hasError -> R.color.color_input_bg_error
            else -> R.color.color_bg_secondary
        }
        ContextCompat.getColorStateList(context, strokeColorRes)?.apply {
            setBoxStrokeColorStateList(this)
        }
        boxBackgroundColor = ContextCompat.getColor(context, bgColorRes)
    }
}