package stud.euktop.uikit.components.input.select

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.FragmentManager
import stud.euktop.uikit.R
import stud.euktop.uikit.components.adapter.SchJTextAdapter
import stud.euktop.uikit.components.base.SchJBaseBinding
import stud.euktop.uikit.components.base.SchJState
import stud.euktop.uikit.components.bottomsheet.SchJBottomSheet
import stud.euktop.uikit.databinding.LayoutSelectBinding
import stud.euktop.uikit.util.setHintUnique
import stud.euktop.uikit.util.setTextUnique

class SchJSelect @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attr, defStyleAttr), SchJState<SchJSelect.State> {
    private val base = object : SchJBaseBinding<LayoutSelectBinding, State>() {
        override fun initBinding() =
            LayoutSelectBinding.inflate(LayoutInflater.from(context), this@SchJSelect, true)

        override fun initState() = State()

        override fun updateState(state: State) {
            binding.root.editText?.apply {
                setTextUnique(state.selectText)
                setHintUnique(state.title)
            }
        }

        override fun setupUI() {
            binding.root.editText?.setOnClickListener { showSheet() }
        }

        init {
            context.withStyledAttributes(attr, R.styleable.SchJSelect) {
                state = state.copy(
                    title = getString(R.styleable.SchJSelect_android_title) ?: state.title
                )
            }
        }
    }
    private var adapter: SchJTextAdapter<*>? = null
    private var bottomSheet: SchJBottomSheet? = null
    private var fragmentManager: FragmentManager? = null
    fun showSheet() {
        val fm = fragmentManager ?: return
        val adapter = adapter ?: return
        val tag = this.hashCode().toString()
        if (fm.findFragmentByTag(tag) == null) {
            bottomSheet = SchJSelectSheet(adapter)
            bottomSheet?.show(fm, tag)
        }
    }

    fun <T> register(
        fragmentManager: FragmentManager,
        items: List<T>,
        onClick: (T) -> Unit,
        toText: (T) -> String,
        isEquals: (value1: T, value2: T) -> Boolean = { p1, p2 -> p1 == p2 }
    ) {
        this.fragmentManager = fragmentManager
        val listener = object : SchJTextAdapter.Listener<T> {
            override fun onClick(value: T) {
                onClick(value)
                state = state.copy(selectText = toText(value))
                bottomSheet?.dismiss()
            }

            override fun toText(value: T): String {
                return toText(value)
            }

            override fun isEquals(value1: T, value2: T): Boolean {
                return isEquals(value1, value2)
            }

        }
        adapter = SchJTextAdapter(listener).apply { submitList(items) }
    }

    override var state: State by base

    data class State(
        val title: String? = null,
        val selectText: String? = null
    )
}