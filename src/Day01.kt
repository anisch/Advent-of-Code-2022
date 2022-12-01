fun main() {
    fun part1(input: List<String>): Int {
        return input
            .joinToString(";") { it }
            .split(";;")
            .maxOf { elf ->
                elf
                    .split(";")
                    .sumOf { it.toInt() }
            }
    }

    fun part2(input: List<String>): Int {
        return input
            .joinToString(";") { it }
            .split(";;")
            .map { elf ->
                elf
                    .split(";")
                    .sumOf { it.toInt() }
            }
            .sortedDescending()
            .take(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val input = readInput("Day01")

    check(part1(testInput) == 24000)
    println(part1(input))

    check(part2(testInput) == 45000)
    println(part2(input))
}
