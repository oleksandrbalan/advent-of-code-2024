fun main() {
    val input = readInput("Day06")

    val data = Day06.parse(input)
    println(Day06.part1(data))
    println(Day06.part2(data))
}

private object Day06 {

    fun parse(input: List<String>): Data {
        val guards = mutableSetOf<Guard>()
        val obstacles = mutableSetOf<Position>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                when (char) {
                    '^' -> guards.add(
                        Guard(
                            position = Position(x, y),
                            direction = Direction.UP,
                        )
                    )

                    '#' -> obstacles.add(Position(x, y))
                }
            }
        }

        return Data(
            guard = guards.first(),
            plan = Plan(
                width = input.first().length,
                height = input.size,
                obstacles = obstacles,
            )
        )
    }


    fun part1(data: Data): Int {
        val visited = mutableSetOf<Position>()
        var guard = data.guard
        while (data.plan.isInBounds(guard.position)) {
            visited.add(guard.position)
            guard = data.plan.move(guard)
        }
        return visited.size
    }

    fun part2(data: Data): Int {
        var count = 0
        repeat(data.plan.height) { y ->
            repeat(data.plan.width) { x ->
                if (x != data.guard.position.x || y != data.guard.position.y) {
                    val newData = Data(
                        guard = data.guard,
                        plan = Plan(
                            width = data.plan.width,
                            height = data.plan.height,
                            obstacles = data.plan.obstacles + Position(x, y)
                        )
                    )
                    count += if (simulate(newData)) 1 else 0
                }
            }
        }
        return count
    }

    fun simulate(data: Data): Boolean {
        val guardLocations = mutableSetOf<Guard>()
        var guard = data.guard
        while (data.plan.isInBounds(guard.position)) {
            if (guardLocations.contains(guard)) {
                return true
            }
            guardLocations.add(guard)
            guard = data.plan.move(guard)
        }
        return false
    }

    class Data(
        val guard: Guard,
        val plan: Plan,
    )

    class Plan(
        val width: Int,
        val height: Int,
        val obstacles: Set<Position>
    ) {
        fun isInBounds(position: Position): Boolean =
            position.x in 0 until width && position.y in 0 until height

        fun move(guard: Guard): Guard {
            val newGuard = guard.move()
            if (!obstacles.contains(newGuard.position)) {
                return newGuard
            }

            return guard.copy(direction = guard.direction.next())
        }
    }

    data class Guard(
        val position: Position,
        val direction: Direction,
    ) {
        fun move(): Guard {
            val position = Position(
                x = when (direction) {
                    Direction.LEFT -> position.x - 1
                    Direction.RIGHT -> position.x + 1
                    else -> position.x
                },
                y = when (direction) {
                    Direction.UP -> position.y - 1
                    Direction.DOWN -> position.y + 1
                    else -> position.y
                }
            )
            return Guard(position, direction)
        }
    }

    data class Position(
        val x: Int,
        val y: Int,
    )

    enum class Direction {
        UP, RIGHT, DOWN, LEFT;

        fun next(): Direction =
            Direction.entries[(ordinal + 1) % Direction.entries.size]
    }
}
