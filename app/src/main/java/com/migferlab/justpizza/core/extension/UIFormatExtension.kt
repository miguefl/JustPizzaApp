package com.migferlab.justpizza.core.extension

import java.text.DecimalFormat

fun Float?.formatUI(): String {
    return when {
        this == null || this == 0.0f -> "0"
        this % 1 == 0.0f -> DecimalFormat("#,##0").format(this)
        else -> DecimalFormat("$#,##0.##").format(this)
    }
}