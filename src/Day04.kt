fun main() {
    val input = readInput("Day04")

    val data = Day04.parse(input)
    println(Day04.part1(data))
    println(Day04.part2(data))
}

private object Day04 {

    fun parse(input: List<String>): Data =
        Data(
            letters = input.map { it.toCharArray().toList() },
        )

    fun part1(data: Data): Int =
        data.indices.sumOf { (row, column) ->
            Direction.all.count { direction ->
                data.hasXmas(row, column, direction)
            }
        }

    fun part2(data: Data): Int =
        data.indices.sumOf { (row, column) ->
            CrossDirection.all.count { direction ->
                data.hasCrossXmas(row, column, direction)
            }
        }

    class Data(
        val letters: List<List<Char>>,
    ) {
        val width: Int = letters.firstOrNull()?.size ?: 0

        val height: Int = letters.size

        val indices: List<Pair<Int, Int>> = List(width * height) { it / width to it % width }

        fun get(row: Int, column: Int): Char? =
            letters.elementAtOrNull(row)?.elementAtOrNull(column)

        fun hasXmas(row: Int, column: Int, direction: Direction): Boolean {
            get(row, column).takeIf { it == 'X' } ?: return false
            get(row + direction.dRow * 1, column + direction.dColumn * 1).takeIf { it == 'M' } ?: return false
            get(row + direction.dRow * 2, column + direction.dColumn * 2).takeIf { it == 'A' } ?: return false
            get(row + direction.dRow * 3, column + direction.dColumn * 3).takeIf { it == 'S' } ?: return false
            return true
        }

        fun hasCrossXmas(row: Int, column: Int, direction: CrossDirection): Boolean {
            get(row, column).takeIf { it == 'A' } ?: return false
            get(row + direction.firstLineRow, column + direction.firstLineColumn).takeIf { it == 'M' } ?: return false
            get(row - direction.firstLineRow, column - direction.firstLineColumn).takeIf { it == 'S' } ?: return false
            get(row + direction.secondLineRow, column + direction.secondLineColumn).takeIf { it == 'M' } ?: return false
            get(row - direction.secondLineRow, column - direction.secondLineColumn).takeIf { it == 'S' } ?: return false
            return true
        }
    }

    data class Direction(
        val dRow: Int,
        val dColumn: Int,
    ) {
        companion object {
            val all: List<Direction> = listOf(
                Direction(-1, -1),
                Direction(-1, +0),
                Direction(-1, +1),
                Direction(+0, -1),
                Direction(+0, +1),
                Direction(+1, -1),
                Direction(+1, +0),
                Direction(+1, +1),
            )
        }
    }

    data class CrossDirection(
        val firstLineRow: Int,
        val firstLineColumn: Int,
        val secondLineRow: Int,
        val secondLineColumn: Int,
    ) {
        companion object {
            val all: List<CrossDirection> = listOf(
                CrossDirection(-1, -1, -1, +1),
                CrossDirection(-1, -1, +1, -1),
                CrossDirection(+1, +1, -1, +1),
                CrossDirection(+1, +1, +1, -1),
            )
        }
    }
}
