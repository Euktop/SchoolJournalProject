package stud.euktop.uikit.util

import android.content.res.Resources

class PxDpSp {
    private val metrics get() = Resources.getSystem().displayMetrics
    private var _px = 0f
    private var _dp = 0f
    private var _sp = 0f
    var px: Float
        get() = _px
        set(value) {
            _px = value
            _dp = value / metrics.density
            _sp = value / metrics.scaledDensity
        }
    var sp: Float
        get() = _sp
        set(value) {
            _sp = value
            _px = value * metrics.scaledDensity
            _dp = _px / metrics.density
        }
    var dp: Float
        get() = _dp
        set(value) {
            _dp = value
            _px = value * metrics.density
            _sp = _px / metrics.scaledDensity
        }

    companion object {
        fun px(px: Float) = PxDpSp().apply { this.px = px }
        fun dp(dp: Float) = PxDpSp().apply { this.dp = dp }
        fun sp(sp: Float) = PxDpSp().apply { this.sp = sp }
    }
}