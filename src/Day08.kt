typealias Forest = List<List<Tree>>

data class Tree(
    val height: Int,
    var isVisible: Boolean = false,
    var score: Int = 0,
)

fun createForest(input: List<String>): Forest = input
    .map { line ->
        line.chunked(1).map { s -> Tree(s.toInt()) }
    }

fun Forest.checkTreeVisibility() {
    // from left to right side
    (indices).forEach { row ->
        var max = -1
        (this[row].indices).forEach { col ->
            if (this[row][col].height > max) {
                max = this[row][col].height
                this[row][col].isVisible = true
            }
        }
    }
    // from right to left side
    (indices).forEach { row ->
        var max = -1
        (this[row].indices.reversed()).forEach { col ->
            if (this[row][col].height > max) {
                max = this[row][col].height
                this[row][col].isVisible = true
            }
        }
    }
    // from top to bottom side
    (this[0].indices).forEach { col ->
        var max = -1
        (this[col].indices).forEach { row ->
            if (this[row][col].height > max) {
                max = this[row][col].height
                this[row][col].isVisible = true
            }
        }
    }

    // from bottom to top side
    (this[0].indices).forEach { col ->
        var max = -1
        (this[col].indices.reversed()).forEach { row ->
            if (this[row][col].height > max) {
                max = this[row][col].height
                this[row][col].isVisible = true
            }
        }
    }
}

fun Forest.calcTreeScores() {
    (1 until this.size - 1).forEach { row ->
        (1 until this[0].size - 1).forEach { col ->
            val ch = this[row][col].height

            // looking right
            var ix = 0
            var max = 0
            for (c in col + 1 until this[row].size) {
                ix++
                val next = this[row][c].height
                if (next >= ch) break
                if (max <= next) max = next
            }

            // looking left
            var jx = 0
            max = 0
            for (c in col - 1 downTo 0) {
                jx++
                val next = this[row][c].height
                if (next >= ch) break
                if (max <= next) max = next
            }

            // looking down
            var kx = 0
            max = 0
            for (r in row + 1 until this.size) {
                kx++
                val next = this[r][col].height
                if (next >= ch) break
                if (max <= next) max = next
            }

            // looking up
            var lx = 0
            max = 0
            for (r in row - 1 downTo 0) {
                lx++
                val next = this[r][col].height
                if (next >= ch) break
                if (max <= next) max = next
            }

            this[row][col].score = ix * jx * kx * lx
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val forest = createForest(input)
        forest.checkTreeVisibility()

        return forest.sumOf { row -> row.count { tree -> tree.isVisible } }
    }

    fun part2(input: List<String>): Int {
        val forest = createForest(input)
        forest.checkTreeVisibility()
        forest.calcTreeScores()

        return forest.maxOf { row -> row.maxOf { tree -> tree.score } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val input = readInput("Day08")

    check(part1(testInput) == 21)
    println(part1(input))

    check(part2(testInput) == 8)
    println(part2(input))
}
