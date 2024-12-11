fun main() {
    val input = readInput("Day08")

    val data = Day08.parse(input)
    println(Day08.part1(data))
    println(Day08.part2(data))
}

private object Day08 {

    fun parse(input: List<String>): Data {
        val antennas = mutableListOf<Antenna>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char != '.') {
                    antennas.add(
                        Antenna(
                            signal = char,
                            position = Position(x, y)
                        )
                    )
                }
            }
        }

        return Data(
            width = input.first().length,
            height = input.size,
            antennas = antennas,
        )
    }

    fun part1(data: Data): Int {
        val positions = mutableSetOf<Position>()
        data.antennas
            .groupBy { it.signal }
            .values
            .forEach { antennas ->
                antennas.forEach { first ->
                    antennas.forEach { second ->
                        if (first != second) {
                            val vector = first.position.vectorTo(second.position)
                            val position1 = first.position + vector.inverse()
                            if (data.isInBounds(position1)) {
                                positions.add(position1)
                            }

                            val position2 = second.position + vector
                            if (data.isInBounds(position2)) {
                                positions.add(position2)
                            }
                        }
                    }
                }
            }
        return positions.size
    }

    fun part2(data: Data): Int {
        val positions = mutableSetOf<Position>()
        data.antennas
            .groupBy { it.signal }
            .values
            .forEach { antennas ->
                antennas.forEach { first ->
                    antennas.forEach { second ->
                        if (first != second) {
                            val vector = first.position.vectorTo(second.position)
                            generateSequence(first.position) { it + vector.inverse() }
                                .plus(generateSequence(second.position) { it + vector })
                                .takeWhile(data::isInBounds)
                                .forEach(positions::add)
                        } else {
                            positions.add(first.position)
                        }
                    }
                }
            }
        return positions.size
    }

    data class Data(
        val width: Int,
        val height: Int,
        val antennas: List<Antenna>,
    ) {
        fun isInBounds(position: Position): Boolean =
            position.x in 0 until width && position.y in 0 until height
    }

    data class Antenna(
        val signal: Char,
        val position: Position,
    )

    data class Position(
        val x: Int,
        val y: Int,
    ) {
        fun vectorTo(other: Position): Vector =
            Vector(
                x = other.x - x,
                y = other.y - y,
            )

        operator fun plus(vector: Vector): Position =
            Position(
                x = x + vector.x,
                y = y + vector.y,
            )
    }

    data class Vector(
        val x: Int,
        val y: Int,
    ) {
        fun inverse(): Vector =
            Vector(
                x = -x,
                y = -y,
            )
    }
}
