package com.aurora.gitsearch.ui.extension

import android.content.res.Resources
import android.util.TypedValue

fun Resources.dp(value: Number): Int = dpf(value).toInt()
fun Resources.dpf(value: Number): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), displayMetrics)
}
