fun findMatch(s1: String, s2: String): Char {
    for (c in 'A'..'z') {
        if (c in s1 && c in s2) return c
    }
    return error("wtf???")
}

fun findMatch(s1: String, s2: String, s3: String): Char {
    for (c in 'A'..'z') {
        if (c in s1 && c in s2 && c in s3) return c
    }
    return error("wtf???")
}

fun getPriority(c: Char): Int {
    return if (c.isLowerCase()) c - 'a' + 1
    else c - 'A' + 27
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { r -> r.chunked(r.length shr 1) }
            .map { (f, s) -> findMatch(f, s) }
            .sumOf { c -> getPriority(c) }
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .map { (r1, r2, r3) -> findMatch(r1, r2, r3) }
            .sumOf { c -> getPriority(c) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    check(part1(testInput) == 157)
    println(part1(input))

    check(part2(testInput) == 70)
    println(part2(input))
}
