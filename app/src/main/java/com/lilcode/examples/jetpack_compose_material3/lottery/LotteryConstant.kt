package com.lilcode.examples.jetpack_compose_material3.lottery

import androidx.compose.ui.graphics.Color
import kotlin.random.Random


const val goldenDp = 1.618

val colorMap = mutableMapOf(
    1 to Color(0x95FF0000),
    2 to Color(0x95FF8500),
    3 to Color(0x959C7501),
    4 to Color(0x957C8000),
    5 to Color(0x95368D00),
    6 to Color(0x95008547),
    7 to Color(0x9500CCFF),
    8 to Color(0x950073FF),
    9 to Color(0x956200FF),
    0 to Color(0x95BB00FF)
).apply {
    (10..45).forEach {
        this[it] = Color(Random.nextInt())
    }
}