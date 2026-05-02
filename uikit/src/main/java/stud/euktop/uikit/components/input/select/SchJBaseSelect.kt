package stud.euktop.uikit.components.input.select

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.google.android.material.textfield.TextInputLayout
import stud.euktop.uikit.R
import stud.euktop.uikit.components.base.SchJBaseBinding
import stud.euktop.uikit.components.base.SchJState
import stud.euktop.uikit.databinding.LayoutSelectBinding
import stud.euktop.uikit.util.setTextUnique
import stud.euktop.uikit.util.setHintUnique

/**
 * Базовый класс для компонентов выбора.
 * 
 * Содержит поле ввода, заголовок и состояние выбранного текста.
 * Наследники отвечают за открытие своего диалога и преобразование типов.
 */
abstract class SchJBaseSelect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SchJState<SchJBaseSelectState> {

    private val baseBinding = object : SchJBaseBinding<LayoutSelectBinding, SchJBaseSelectState>() {
        override fun initBinding() =
            LayoutSelectBinding.inflate(LayoutInflater.from(context), this@SchJBaseSelect, true)

        override fun initState() = SchJBaseSelectState()

        override fun updateState(state: SchJBaseSelectState) {
            binding.root.editText?.apply {
                setTextUnique(state.selectText)
                setHintUnique(state.title)
            }
            binding.root.apply {
                endIconDrawable = if (state.selectText.isNullOrBlank()) {
                    ContextCompat.getDrawable(context, R.drawable.right_r)
                } else {
                    ContextCompat.getDrawable(context, R.drawable.ic_close_select)
                }
            }
        }

        override fun setupUI() {
            binding.root.editText?.setOnClickListener {
                onShowing?.invoke()
                showSelectionDialog()
            }
            binding.root.setEndIconOnClickListener {
                setSelectedText("")
                onClearText()
            }
        }

        init {
            context.withStyledAttributes(attrs, R.styleable.SchJBaseSelect) {
                state = state.copy(
                    title = getString(R.styleable.SchJBaseSelect_android_title) ?: state.title
                )
            }
        }
    }

    protected abstract fun onClearText()

    override var state: SchJBaseSelectState by baseBinding

    /**
     * Открывает диалог выбора.
     * Наследники должны реализовать этот метод.
     */
    protected abstract fun showSelectionDialog()

    var onShowing: (() -> Unit)? = null

    /**
     * Устанавливает выбранный текст после закрытия диалога.
     * @param selectedText текст, который будет отображён в поле ввода
     */
    protected fun setSelectedText(selectedText: String) {
        state = state.copy(selectText = selectedText)
    }
}