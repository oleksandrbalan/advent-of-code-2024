fun main() {
    val input = readInput("Day02")

    val data = Day02.parse(input)
    println(Day02.part1(data))
    println(Day02.part2(data))
}

private object Day02 {

    fun parse(input: List<String>): Data {
        val reports = input.map { line ->
            val levels = line.split(" ")
            Report(
                levels = levels.map { it.toInt() },
            )
        }

        return Data(
            reports = reports,
        )
    }

    fun part1(data: Data): Int =
        data.reports.count { report ->
            report.isSafe()
        }

    fun part2(data: Data): Int =
        data.reports.count { report ->
            report.isSafe() || report.levels.indices.any { report.withoutIndex(it).isSafe() }
        }

    data class Data(
        val reports: List<Report>,
    )

    data class Report(
        val levels: List<Int>,
    ) {
        fun isSafe(): Boolean =
            levels.asSequence()
                .windowed(2)
                .map { it.last() - it.first() }
                .let { diffs -> diffs.all { it in 1..3 } || diffs.all { it in -3..-1 } }

        fun withoutIndex(index: Int): Report =
            Report(
                levels = levels.subList(0, index) + levels.subList(index + 1, levels.count())
            )
    }
}
