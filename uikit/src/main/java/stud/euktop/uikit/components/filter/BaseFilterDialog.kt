package stud.euktop.uikit.components.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import stud.euktop.uikit.databinding.DialogFilterBaseBinding

/**
 * Базовый абстрактный диалог для фильтрации списков.
 * @param T тип объекта фильтра (например, HomeworkFilter).
 */
abstract class BaseFilterDialog<T> : DialogFragment() {

    protected lateinit var binding: DialogFilterBaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFilterBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFilterFields(binding.containerFields)
        binding.btnReset.setOnClickListener { resetFilters() }
        binding.btnApply.setOnClickListener {
            val filter = collectFilter()
            onApply(filter)
            dismiss()
        }
    }

    /** Добавить поля фильтрации в контейнер */
    protected abstract fun setupFilterFields(container: LinearLayout)

    /** Сбросить значения полей к начальным */
    protected open fun resetFilters() {}

    /** Собрать значения полей в объект фильтра */
    protected abstract fun collectFilter(): T

    /** Применить фильтр */
    protected abstract fun onApply(filter: T)
}