package stud.euktop.uikit.components.input

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import stud.euktop.uikit.R

open class SchJTextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle,
) : TextInputEditText(context, attrs, defStyleAttr) {
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (this.text.toString() == text.toString()) return
        super.setText(text, type)
    }
}