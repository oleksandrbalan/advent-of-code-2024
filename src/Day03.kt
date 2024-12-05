fun main() {
    val input = readInput("Day03")

    val data = Day03.parse(input)
    println(Day03.part1(data))
    println(Day03.part2(data))
}

private object Day03 {

    fun parse(input: List<String>): Data {
        val commands = "(mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\))".toRegex()
            .findAll(input.joinToString(""))
            .map { Command.from(it.value) }
            .toList()

        return Data(
            commands = commands,
        )
    }

    fun part1(data: Data): Int =
        data.commands
            .filterIsInstance<Command.Multiply>()
            .sumOf { it.left * it.right }

    fun part2(data: Data): Int {
        val accumulator = data.commands.fold(Accumulator(0, true)) { acc, command ->
            when (command) {
                is Command.ChangeState -> acc.copy(enabled = command.enabled)
                is Command.Multiply -> if (acc.enabled) {
                    acc.copy(sum = acc.sum + command.left * command.right)
                } else {
                    acc
                }
            }
        }
        return accumulator.sum
    }

    data class Data(
        val commands: List<Command>,
    )

    sealed interface Command {
        data class Multiply(val left: Int, val right: Int) : Command
        data class ChangeState(val enabled: Boolean) : Command

        companion object {

            fun from(value: String): Command =
                when (value) {
                    "do()" -> ChangeState(true)
                    "don't()" -> ChangeState(false)
                    else -> requireNotNull("(\\d{1,3}),(\\d{1,3})".toRegex().find(value))
                        .groupValues.let { Multiply(it[1].toInt(), it[2].toInt()) }
                }
        }
    }

    private data class Accumulator(
        val sum: Int,
        val enabled: Boolean,
    )
}
