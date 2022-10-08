package com.lilcode.examples.jetpack_compose_material3.lottery

import androidx.compose.ui.graphics.Color
import kotlin.random.Random


val Int.lotteryColor
    get() = colorMap[this] ?: Color(Random.nextInt())
