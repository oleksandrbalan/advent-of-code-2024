import Day07.Operator.CONCAT
import Day07.Operator.PLUS
import Day07.Operator.TIMES
import kotlin.math.pow

fun main() {
    val input = readInput("Day07")

    val data = Day07.parse(input)
    println(Day07.part1(data))
    println(Day07.part2(data))
}

private object Day07 {

    fun parse(input: List<String>): Data {
        return Data(
            equations = input.map { line ->
                val (result, numbers) = line.split(":")
                Equation(
                    result = result.toLong(),
                    numbers = numbers.trim().split(" ").map { it.toLong() }
                )
            }
        )
    }

    fun part1(data: Data): Long =
        data.equations
            .filter { it.canBeMadeTrue(listOf(PLUS, TIMES)) }
            .sumOf { it.result }

    fun part2(data: Data): Long =
        data.equations
            .filter { it.canBeMadeTrue(listOf(PLUS, TIMES, CONCAT)) }
            .sumOf { it.result }

    class Data(
        val equations: List<Equation>,
    )

    data class Equation(
        val result: Long,
        val numbers: List<Long>,
    ) {
        fun canBeMadeTrue(operators: List<Operator>): Boolean =
            (0 until operators.size.toDouble().pow(numbers.size - 1).toInt())
                .any { bits ->
                    val operations = bits.toString(operators.size)
                        .padStart(numbers.size - 1, '0')
                        .map { operators[it.digitToInt()] }

                    val actual = numbers
                        .drop(1)
                        .foldIndexed(numbers.first()) { index, acc, i -> operations[index].calculate(acc, i) }

                    actual == result
                }
    }

    enum class Operator {
        PLUS, TIMES, CONCAT;

        fun calculate(num1: Long, num2: Long): Long =
            when (this) {
                PLUS -> num1 + num2
                TIMES -> num1 * num2
                CONCAT -> "$num1$num2".toLong()
            }
    }
}
