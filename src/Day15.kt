import kotlin.math.abs

private fun getPositions(input: List<String>) = input
    .map { line ->
        val (s, b) = line.split(": ")
        val (xs, ys) = s.removeRange(0..9).split(",")
        val (xb, yb) = b.removeRange(0..20).split(",")
        val vecS = Vec(
            xs.substringAfter("=").toInt(),
            ys.substringAfter("=").toInt(),
        )
        val vecB = Vec(
            xb.substringAfter("=").toInt(),
            yb.substringAfter("=").toInt(),
        )
        Pair(vecS, vecB)
    }

private fun getRanges(positions: List<Pair<Vec, Vec>>, row: Int): List<IntRange> = positions
    .mapNotNull { (s, b) ->
        val distance = abs(s.x - b.x) + abs(s.y - b.y)
        val yRange = s.y - distance..s.y + distance

        if (row in yRange) {
            val yd = abs(s.y - row)
            val range = (distance - yd)
            s.x - range..s.x + range
        } else null
    }
    .sortedBy { it.first }

fun main() {
    fun part1(input: List<String>, row: Int): Int {
        val positions = getPositions(input)
        val ranges = getRanges(positions, row)
            .reduce { acc, intRange ->
                val start =
                    if (intRange.first < acc.first) intRange.first
                    else acc.first
                val end =
                    if (intRange.last > acc.last) intRange.last
                    else acc.last

                start..end
            }
        val nothing = ranges.count()

        val beacons = positions
            .filter { (_, b) -> b.y == row && b.x in ranges }
            .map { it.second }
            .toSet()
            .size

        return nothing - beacons
    }

    fun part2(input: List<String>, rows: Int): Long {
        val positions = getPositions(input)

        var vec = Vec(0, 0)
        for (row in 0..rows) {
            val ranges = getRanges(positions, row)

            var col: Int? = null
            var lastX = ranges.first().last
            for (idx in 1 until ranges.size) {
                if (lastX + 1 < ranges[idx].first) {
                    col = lastX + 1
                } else {
                    lastX =
                        if (lastX >= ranges[idx].last) lastX
                        else ranges[idx].last
                }
            }
            if (col != null) {
                vec = Vec(col, row)
                break
            }
        }

        return vec.x.toLong() * 4_000_000L + vec.y.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    val input = readInput("Day15")

    check(part1(testInput, 10) == 26)
    println(part1(input, 2_000_000))

    check(part2(testInput, 20) == 56_000_011L)
    println(part2(input, 4_000_000))
}
