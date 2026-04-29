package stud.euktop.uikit.components.lineChart

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import androidx.core.content.ContextCompat
import stud.euktop.uikit.R
import stud.euktop.uikit.components.base.SchJBaseBinding
import stud.euktop.uikit.components.base.SchJState
import stud.euktop.uikit.databinding.LayoutLineChartBinding

class SchJLineChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), SchJState<SchJLineChartState> {

    private val base = object : SchJBaseBinding<LayoutLineChartBinding, SchJLineChartState>() {
        override fun initBinding() = LayoutLineChartBinding.inflate(
            android.view.LayoutInflater.from(context), this@SchJLineChart, true
        )

        override fun initState() = SchJLineChartState()

        override fun updateState(state: SchJLineChartState) {
            val entries = state.points.mapIndexed { index, point ->
                Entry(index.toFloat(), point.value, point.label)
            }
            val dataSet = LineDataSet(entries, "").apply {
                color = ContextCompat.getColor(context, R.color.color_accent)
                valueTextColor = ContextCompat.getColor(context, R.color.color_text_secondary)
                lineWidth = resources.getDimension(R.dimen.chart_line_width)
                circleRadius = resources.getDimension(R.dimen.chart_circle_radius)
                setDrawValues(true)
                valueTextSize = resources.getDimension(R.dimen.chart_value_text_size)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            binding.lineChart.apply {
                data = LineData(dataSet)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(state.points.map { it.label })
                    setDrawGridLines(false)
                    textColor = ContextCompat.getColor(context, R.color.color_text_secondary)
                }
                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                animateX(500)
                invalidate()
            }
        }

        override fun setupUI() {}
    }

    override var state: SchJLineChartState by base
}