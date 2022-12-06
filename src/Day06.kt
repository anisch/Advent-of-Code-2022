fun String.hasUniqueChars(): Boolean = toSet().size == length

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .first()
            .windowed(4)
            .indexOfFirst { it.hasUniqueChars() } + 4
    }

    fun part2(input: List<String>): Int {
        return input
            .first()
            .windowed(14)
            .indexOfFirst { it.hasUniqueChars() } + 14
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    check(part1(testInput) == 11)
    println(part1(input))

    check(part2(testInput) == 26)
    println(part2(input))
}
