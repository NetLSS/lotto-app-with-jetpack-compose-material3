package com.lilcode.examples.jetpack_compose_material3.lottery

import androidx.lifecycle.MutableLiveData
import com.lilcode.examples.jetpack_compose_material3.common.toMutableLiveData

object LotteryHelper {
    fun get720Numbers(): Data720 {
        val groups = MutableList(5) { it + 1 }
        repeat(7) {
            groups.shuffle()
        }
        val group = groups.random()

        val numbers = MutableList(10) { it }
        val luckyNumbers = mutableListOf<Int>()

        repeat(6) {
            repeat(7) {
                numbers.shuffle()
            }
            luckyNumbers.add(numbers.random())
        }

        return Data720(
            groupNumber = group,
            numbers = luckyNumbers
        )
    }

    fun get645Numbers(): Data645 {
        val numbers = MutableList(45) { it + 1 }

        val luckyNumbers = mutableListOf<Int>()

        repeat(6) {
            repeat(7) {
                numbers.shuffle()
            }
            val luckyNumber = numbers.random()
            numbers.remove(luckyNumber)
            luckyNumbers.add(luckyNumber)
        }

        return Data645(luckyNumbers)
    }

    data class Data645(
        val numbers: List<Int>,
        val isCanceled: MutableLiveData<Boolean> = false.toMutableLiveData()
    )

    data class Data720(
        val groupNumber: Int,
        val numbers: List<Int>,
        val isCanceled: MutableLiveData<Boolean> = false.toMutableLiveData()
    )

    val defaultData720
        get() = Data720(1, listOf(1, 2, 3, 4, 5, 6))

    val defaultData645
        get() = Data645(listOf(1, 2, 3, 4, 5, 6))
}