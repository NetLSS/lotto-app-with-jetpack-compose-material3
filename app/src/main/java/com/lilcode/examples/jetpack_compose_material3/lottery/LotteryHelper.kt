package com.lilcode.examples.jetpack_compose_material3.lottery

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

    data class Data720(
        val groupNumber: Int,
        val numbers: List<Int>
    )

    val defaultData720
        get() = Data720(1, listOf(1, 2, 3, 4, 5, 6))
}