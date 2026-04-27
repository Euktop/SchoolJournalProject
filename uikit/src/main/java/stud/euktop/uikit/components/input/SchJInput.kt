package stud.euktop.uikit.components.input

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.google.android.material.textfield.TextInputLayout
import stud.euktop.uikit.R
import stud.euktop.uikit.components.base.SchJBaseBinding
import stud.euktop.uikit.components.base.SchJState
import stud.euktop.uikit.databinding.LayoutTextInputBinding

class SchJInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), SchJState<SchJInput.State> {
    init {
        orientation = VERTICAL
    }

    private val schJBase = object : SchJBaseBinding<LayoutTextInputBinding, State>() {
        override fun initBinding() =
            LayoutTextInputBinding.inflate(LayoutInflater.from(context), this@SchJInput)

        override fun initState() = State()

        override fun updateState(state: State) {
            binding.apply {
                editText.apply {
                    setText(state.text)
                    hint = state.textHint
                    inputType = state.inputType
                    updateStrokeAndBackground(hasFocus(), state.errorVisible())
                }
                inputLayout.endIconMode = state.endIconMode
                helperText.apply {
                    text = state.textHelper
                    visibility = if (state.textHelper.isEmpty()) GONE else VISIBLE
                }
                errorText.apply {
                    text = state.textError
                    visibility = if (state.errorVisible()) VISIBLE else GONE
                }
            }
        }

        override fun setupUI() {
            binding.editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (_state.text != s.toString()) {
                        _state = _state.copy(text = s.toString())
                        listener?.afterTextChanged(s.toString())
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.editText.setOnFocusChangeListener { _, hasFocus ->
                updateStrokeAndBackground(hasFocus, _state.errorVisible())
            }
        }

        private fun updateStrokeAndBackground(hasFocus: Boolean, hasError: Boolean) {
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
                binding.inputLayout.setBoxStrokeColorStateList(this)
            }
            binding.inputLayout.boxBackgroundColor = ContextCompat.getColor(context, bgColorRes)
        }

        init {
            context.withStyledAttributes(attrs, R.styleable.SchJInput) {
                state = State(
                    text = getString(R.styleable.SchJInput_android_text) ?: state.text,
                    textHint = getString(R.styleable.SchJInput_android_hint) ?: state.textHint,
                    textHelper = getString(R.styleable.SchJInput_helperText)
                        ?: state.textHelper,
                    endIconMode = getInt(R.styleable.SchJInput_endIconMode, state.endIconMode),
                    textError = getString(R.styleable.SchJInput_schjTextError) ?: state.textError,
                    isErrorVisible = getBoolean(
                        R.styleable.SchJInput_errorEnabled,
                        state.isErrorVisible
                    ),
                    inputType = getInt(R.styleable.SchJInput_android_inputType, state.inputType)
                )
            }
        }
    }

    fun interface Listener {
        fun afterTextChanged(text: String)
    }

    var listener: Listener? = null
    override var state: State by schJBase

    data class State(
        val text: String = "",
        val textHint: String = "",
        val textHelper: String = "",
        val textError: String = "",
        val isErrorVisible: Boolean = false,
        val endIconMode: Int = TextInputLayout.END_ICON_NONE,
        val inputType: Int = InputType.TYPE_CLASS_TEXT
    ) {
        fun errorVisible() = textError.isNotEmpty() && isErrorVisible
    }
}