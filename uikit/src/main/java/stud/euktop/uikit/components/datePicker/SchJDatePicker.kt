package stud.euktop.uikit.components.datePicker

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import stud.euktop.uikit.components.base.SchJBase
import stud.euktop.uikit.components.base.SchJState
import java.util.*

/**
 * Кастомный DatePickerDialog с предустановленными ограничениями:
 * - Максимальная дата – сегодня
 * - Минимальная дата – 100 лет назад от сегодня
 * - Предотвращение повторного показа (через showUnique())
 */
class SchJDatePicker(
    context: Context,
    private val onDateSelected: (Date) -> Unit
) : DatePickerDialog(
    context
), SchJState<SchJDatePicker.State> {

    // Состояние диалога
    data class State(
        val selectedDate: Date? = null,
        val minDateMillis: Long = 0L,
        val maxDateMillis: Long = 0L
    )

    private val base = object : SchJBase<State>() {
        override fun initState() = State()

        override fun updateState(state: State) {
            state.minDateMillis.takeIf { it > 0 }?.let { datePicker.minDate = it }
            state.maxDateMillis.takeIf { it > 0 }?.let { datePicker.maxDate = it }
            state.selectedDate?.let {
                val c = Calendar.getInstance().apply { time = it }
                datePicker.updateDate(
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                )
            }
        }

        override fun setupUI() {
            val calendar = Calendar.getInstance()
            val maxDate = calendar.timeInMillis
            calendar.add(Calendar.YEAR, -100)
            val minDate = calendar.timeInMillis

            datePicker.maxDate = maxDate
            datePicker.minDate = minDate

            // Сохраняем в state для возможности восстановления
            state = state.copy(
                minDateMillis = minDate,
                maxDateMillis = maxDate
            )
        }
    }

    override var state: State by base

    init {
        setOnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selected = GregorianCalendar(year, month, dayOfMonth).time
            state = state.copy(selectedDate = selected)
            onDateSelected(selected)
        }
    }

    fun showUnique() {
        if (!isShowing) {
            show()
        }
    }
}