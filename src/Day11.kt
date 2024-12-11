fun main() {
    val input = readInput("Day11")

    val data = Day11.parse(input)
    println(Day11.part1(data))
    println(Day11.part2(data))
}

private object Day11 {

    fun parse(input: List<String>): Data =
        Data(
            stones = input.first().split(" ").map { it.toLong() }
        )

    fun part1(data: Data): Long =
        data.stones.sumOf {
            data.count(25, it)
        }

    fun part2(data: Data): Long =
        data.stones.sumOf {
            data.count(75, it)
        }

    data class Data(
        val stones: List<Long>,
    ) {
        private val cache: MutableMap<Key, Long> = mutableMapOf()

        fun count(iteration: Int, stone: Long): Long {
            if (iteration == 0) {
                return 1
            }

            val key = Key(iteration, stone)
            val cachedCount = cache[key]
            if (cachedCount != null) {
                return cachedCount
            }

            val stones = blink(stone)
            return stones.sumOf {
                count(iteration - 1, it)
            }.also { count ->
                cache[key] = count
            }
        }

        private fun blink(stone: Long): List<Long> {
            if (stone == 0L) {
                return listOf(1L)
            }

            val string = stone.toString()
            if (string.length % 2 == 0) {
                return listOf(
                    string.substring(0, string.length / 2).toLong(),
                    string.substring(string.length / 2).toLong(),
                )
            }

            return listOf(stone * 2024)
        }

        private data class Key(
            val iteration: Int,
            val stone: Long,
        )
    }
}
