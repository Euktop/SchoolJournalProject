package stud.euktop.uikit.components.input.select

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
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

    protected lateinit var binding: LayoutSelectBinding

    private val baseBinding = object : SchJBaseBinding<LayoutSelectBinding, SchJBaseSelectState>() {
        override fun initBinding(): LayoutSelectBinding {
            binding =
                LayoutSelectBinding.inflate(LayoutInflater.from(context), this@SchJBaseSelect, true)
            return binding
        }

        override fun initState() = SchJBaseSelectState()

        override fun updateState(state: SchJBaseSelectState) {
            binding.root.editText?.setTextUnique(state.selectText)
            binding.root.editText?.setHintUnique(state.title)
        }

        override fun setupUI() {
            binding.root.editText?.setOnClickListener { showSelectionDialog() }
        }

        init {
            context.withStyledAttributes(attrs, R.styleable.SchJBaseSelect) {
                state = state.copy(
                    title = getString(R.styleable.SchJBaseSelect_android_title) ?: state.title
                )
            }
        }
    }

    override var state: SchJBaseSelectState by baseBinding

    /**
     * Открывает диалог выбора.
     * Наследники должны реализовать этот метод.
     */
    protected abstract fun showSelectionDialog()

    /**
     * Устанавливает выбранный текст после закрытия диалога.
     * @param selectedText текст, который будет отображён в поле ввода
     */
    protected fun setSelectedText(selectedText: String) {
        state = state.copy(selectText = selectedText)
    }
}