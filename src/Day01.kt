import kotlin.math.abs

fun main() {
    val input = readInput("Day01")

    val data = Day01.parse(input)
    println(Day01.part1(data))
    println(Day01.part2(data))
}

private object Day01 {

    fun parse(input: List<String>): Locations {
        val locations = input.map {
            val pair = it.split("   ")
            pair[0].toInt() to pair[1].toInt()
        }

        return Locations(
            first = locations.map { it.first },
            second = locations.map { it.second },
        )
    }

    fun part1(locations: Locations): Int =
        locations.first.sorted().zip(locations.second.sorted())
            .sumOf { pair -> abs(pair.first - pair.second) }

    fun part2(locations: Locations): Int =
        locations.first.sumOf { location ->
            val times = locations.second.count { it == location }
            location * times
        }

    data class Locations(
        val first: List<Int>,
        val second: List<Int>,
    )
}
