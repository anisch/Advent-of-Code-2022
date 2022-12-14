private typealias Cave = Array<Array<String>>

private fun Cave.isFreeDown(row: Int, col: Int) = this[row + 1][col] == "."
private fun Cave.isFreeLeftDown(row: Int, col: Int) = this[row + 1][col - 1] == "."
private fun Cave.isFreeRightDown(row: Int, col: Int) = this[row + 1][col + 1] == "."
private fun Cave.nextIsNotFree(row: Int, col: Int) =
    this[row + 1][col] != "." && this[row + 1][col - 1] != "." && this[row + 1][col + 1] != "."

private fun Cave.nextIsBottom(row: Int) = row >= lastIndex

private const val START_COL = 500

private fun createCave(input: List<String>): Cave {
    val cave = Array(START_COL + 1) { Array(2 * START_COL + 1) { "." } }
    input
        .map { line -> line.split(" -> ") }
        .map { line ->
            line.map { pos ->
                val row = pos.substringAfter(",").toInt()
                val col = pos.substringBefore(",").toInt()
                Pair(row, col)
            }
        }
        .forEach { line ->
            for (pos in 1..line.lastIndex) {
                val a = line[pos - 1]
                val b = line[pos]

                if (a.first == b.first) { // row == row
                    var f = a.second
                    var t = b.second
                    if (f > t) f = t.also { t = f }
                    for (col in f..t) {
                        cave[a.first][col] = "#"
                    }
                } else { // col == col
                    var f = a.first
                    var t = b.first
                    if (f > t) f = t.also { t = f }
                    for (row in f..t) {
                        cave[row][a.second] = "#"
                    }
                }
            }
        }

    return cave
}

fun main() {
    fun part1(input: List<String>): Int {
        val cave = createCave(input)

        var count = 0
        val startPos = Pair(0, START_COL)
        var current = startPos

        while (!cave.nextIsBottom(current.first)) {
            count++
            while (true) {
                when {
                    cave.nextIsBottom(current.first) -> break

                    cave.isFreeDown(current.first, current.second) -> {
                        current = Pair(current.first + 1, current.second)
                    }

                    cave.isFreeLeftDown(current.first, current.second) -> {
                        current = Pair(current.first + 1, current.second - 1)
                    }

                    cave.isFreeRightDown(current.first, current.second) -> {
                        current = Pair(current.first + 1, current.second + 1)
                    }

                    cave.nextIsNotFree(current.first, current.second) -> {
                        cave[current.first][current.second] = "o"
                        current = startPos
                        break
                    }
                }
            }
        }

        return count - 1
    }

    fun part2(input: List<String>): Int {
        val cave = createCave(input)

        val rowMax = cave
            .mapIndexed { i, s -> if (s.contains("#")) i else 0 }
            .max()

        cave.also { it[rowMax + 2] = Array(2 * START_COL + 1) { "#" } }

        var count = 0
        val startPos = Pair(0, START_COL)
        var current = startPos

        while (!cave.nextIsNotFree(startPos.first, startPos.second)) {
            count++
            while (true) {
                when {
                    cave.isFreeDown(current.first, current.second) -> {
                        current = Pair(current.first + 1, current.second)
                    }

                    cave.isFreeLeftDown(current.first, current.second) -> {
                        current = Pair(current.first + 1, current.second - 1)
                    }

                    cave.isFreeRightDown(current.first, current.second) -> {
                        current = Pair(current.first + 1, current.second + 1)
                    }

                    cave.nextIsNotFree(current.first, current.second) -> {
                        cave[current.first][current.second] = "o"
                        current = startPos
                        break
                    }
                }
            }
        }

        return count + 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    val input = readInput("Day14")

    check(part1(testInput) == 24)
    println(part1(input))

    check(part2(testInput) == 93)
    println(part2(input))
}
