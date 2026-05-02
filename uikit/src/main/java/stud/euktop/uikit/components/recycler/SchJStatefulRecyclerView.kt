package stud.euktop.uikit.components.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.LayoutStatefulRecyclerBinding

/**
 * Компонент, объединяющий RecyclerView и TextView для пустого состояния.
 *
 * Автоматически показывает RecyclerView, если передан непустой список,
 * и TextView с сообщением, если список пуст и загрузка завершена.
 *
 * Пример использования во фрагменте:
 * ```
 * binding.statefulRecycler.setup(adapter)
 * binding.statefulRecycler.submitList(data, isLoading)
 * ```
 */
class SchJStatefulRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutStatefulRecyclerBinding.inflate(LayoutInflater.from(context), this)

    init {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        context.withStyledAttributes(attrs, R.styleable.SchJStatefulRecyclerView) {
            binding.recyclerView.isNestedScrollingEnabled = getBoolean(
                R.styleable.SchJStatefulRecyclerView_android_nestedScrollingEnabled,
                false
            )
        }
    }

    fun update() {
        binding.apply {
            val adapter = recyclerView.adapter
            if (adapter != null && adapter.itemCount > 0) {
                recyclerView.visibility = VISIBLE
                tvEmpty.visibility = GONE
            } else {
                recyclerView.visibility = GONE
                tvEmpty.visibility = VISIBLE
            }
        }
    }

    inline fun update(action: (RecyclerView.Adapter<*>) -> Unit) {
        action(recyclerView.adapter as RecyclerView.Adapter<*>)
        update()
    }

    var adapter: RecyclerView.Adapter<*>?
        get() {
            update()
            return binding.recyclerView.adapter
        }
        set(value) {
            binding.recyclerView.adapter = value
            update()
        }
    val recyclerView
        get() = binding.recyclerView
}