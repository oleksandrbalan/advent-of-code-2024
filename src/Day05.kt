fun main() {
    val input = readInput("Day05")

    val data = Day05.parse(input)
    println(Day05.part1(data))
    println(Day05.part2(data))
}

private object Day05 {

    fun parse(input: List<String>): Data {
        val rules = mutableListOf<Pair<Int, Int>>()
        val updates = mutableListOf<Update>()

        input.forEach { line ->
            when {
                line.contains("|") -> {
                    val (first, second) = line.split("|")
                    rules.add(first.toInt() to second.toInt())
                }

                line.contains(",") -> {
                    val values = line.split(",").map { it.toInt() }
                    updates.add(Update(values))
                }
            }
        }

        return Data(Rules(rules), updates)
    }


    fun part1(data: Data): Int =
        data.updates
            .filter { it.isValid(data.rules) }
            .sumOf { it.middle }

    fun part2(data: Data): Int =
        data.updates
            .filter { !it.isValid(data.rules) }
            .onEach { it.sort(data.rules) }
            .sumOf { it.middle }

    class Data(
        val rules: Rules,
        val updates: List<Update>,
    )

    class Rules(list: List<Pair<Int, Int>>) {

        private val map: Map<Int, Set<Int>> = list
            .groupBy { it.first }
            .mapValues { entry -> entry.value.map { it.second }.toSet() }

        fun satisfies(first: Int, second: Int): Boolean {
            val values = map[first] ?: return false
            return values.contains(second)
        }
    }

    class Update(values: List<Int>) {

        var values: MutableList<Int> = values.toMutableList()

        val middle: Int get() = values[values.size / 2]

        fun isValid(rules: Rules): Boolean =
            values.windowed(2).all { rules.satisfies(it.first(), it.last()) }

        fun swap(index1: Int, index2: Int) {
            val (a, b) = values[index1] to values[index2]
            values[index1] = b
            values[index2] = a
        }

        fun sort(rules: Rules) {
            repeat(values.size) {
                values.indices.toList().dropLast(1).forEach { index ->
                    if (!rules.satisfies(values[index], values[index + 1])) {
                        swap(index, index + 1)
                    }
                }
            }
        }
    }
}
