fun main() {
    val input = readInput("Day09")

    val data = Day09.parse(input)
    println(Day09.part1(data))
    println(Day09.part2(data))
}

private object Day09 {

    fun parse(input: List<String>): Data =
        Data(
            raw = input.first().map { it.digitToInt() },
        )

    fun part1(data: Data): Long {
        val disk = data.disk()
        var index1 = 0
        var index2 = disk.values.lastIndex
        while (index1 != index2) {
            val value1 = disk.values[index1]
            if (value1 != -1) {
                index1 += 1
            } else {
                val value2 = disk.values[index2]
                if (value2 == -1) {
                    index2 -= 1
                } else {
                    disk.values[index1] = value2
                    disk.values[index2] = value1
                }
            }
        }

        return disk.checksum()
    }

    fun part2(data: Data): Long {
        val files = buildList {
            var acc = 0
            data.raw.forEachIndexed { index, i ->
                if (index % 2 == 0) {
                    add(File(index / 2, acc, i))
                }
                acc += i
            }
        }

        val disk = data.disk()
        files.reversed().forEach { file ->
            val space = sequence {
                var start = -1
                disk.values.forEachIndexed { index, value ->
                    if (value == -1 && start == -1) {
                        start = index
                    } else if (value != -1 && start != -1) {
                        yield(FreeSpace(start, index - start))
                        start = -1
                    }
                }
            }.find {
                it.size >= file.size && it.position < file.position
            }

            if (space != null) {
                repeat(file.size) {
                    disk.values[space.position + it] = file.id
                    disk.values[file.position + it] = -1
                }
            }
        }

        return disk.checksum()
    }

    data class Data(
        val raw: List<Int>,
    ) {
        fun disk(): Disk =
            Disk(
                raw.mapIndexed { index, count ->
                    val free = index % 2 == 1
                    if (free) {
                        List(count) { -1 }
                    } else {
                        List(count) { index / 2 }
                    }
                }.flatten().toTypedArray()
            )
    }

    class Disk(
        val values: Array<Int>,
    ) {
        fun checksum(): Long =
            values.foldIndexed(0L) { index, acc, i ->
                acc + index * i.coerceAtLeast(0).toLong()
            }
    }

    data class FreeSpace(
        val position: Int,
        val size: Int,
    )

    data class File(
        val id: Int,
        val position: Int,
        val size: Int,
    )
}
