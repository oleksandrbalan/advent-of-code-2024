fun main() {
    val input = readInput("Day10")

    val data = Day10.parse(input)
    println(Day10.part1(data))
    println(Day10.part2(data))
}

private object Day10 {

    fun parse(input: List<String>): Data {
        val heights = mutableMapOf<Position, Int>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                heights[Position(x, y)] = char.digitToInt()
            }
        }

        return Data(
            heights = heights,
        )
    }

    fun part1(data: Data): Int =
        data.heights.filter { it.value == 0 }.keys.sumOf {
            data.findTops(it).size
        }

    fun part2(data: Data): Int =
        data.heights.filter { it.value == 0 }.keys.sumOf {
            data.findTrails(it).size
        }

    data class Data(
        val heights: Map<Position, Int>
    ) {
        fun findTops(position: Position): Set<Position> {
            val value = heights[position] ?: return emptySet()
            if (value == 9) {
                return setOf(position)
            }

            val topPositions = mutableSetOf<Position>()
            listOf(
                position.copy(y = position.y - 1),
                position.copy(x = position.x + 1),
                position.copy(y = position.y + 1),
                position.copy(x = position.x - 1)
            ).forEach {
                if (value + 1 == heights[it]) {
                    topPositions.addAll(findTops(it))
                }
            }

            return topPositions
        }

        fun findTrails(position: Position): Set<Trail> {
            val value = heights[position] ?: return emptySet()
            if (value == 9) {
                return setOf(Trail(listOf(position)))
            }

            val trails = mutableSetOf<Trail>()
            listOf(
                position.copy(y = position.y - 1),
                position.copy(x = position.x + 1),
                position.copy(y = position.y + 1),
                position.copy(x = position.x - 1)
            ).forEach { neighbour ->
                if (value + 1 == heights[neighbour]) {
                    trails.addAll(
                        findTrails(neighbour).map { trail ->
                            trail.copy(
                                positions = trail.positions + neighbour
                            )
                        }
                    )
                }
            }

            return trails
        }
    }

    data class Trail(
        val positions: List<Position>
    )

    data class Position(
        val x: Int,
        val y: Int,
    )
}
