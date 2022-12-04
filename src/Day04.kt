fun inRange(a: Int, b: Int, c: Int, d: Int): Boolean =
    (c <= a && b <= d) || (a <= c && d <= b)

fun overlaps(a: Int, b: Int, c: Int, d: Int): Boolean =
    (a in c..d) || (b in c..d) || (c in a..b) || (d in a..b)

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.split(",") }
            .map { (f, s) ->
                val (a, b) = f.split("-")
                val (c, d) = s.split("-")
                inRange(a.toInt(), b.toInt(), c.toInt(), d.toInt())
            }
            .count { it }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { it.split(",") }
            .map { (f, s) ->
                val (a, b) = f.split("-")
                val (c, d) = s.split("-")
                overlaps(a.toInt(), b.toInt(), c.toInt(), d.toInt())
            }
            .count { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    check(part1(testInput) == 2)
    println(part1(input))

    check(part2(testInput) == 4)
    println(part2(input))
}
