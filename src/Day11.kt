data class Monkey(
    val name: Int,
    val items: MutableList<Long>,
    val op: (Long) -> Long,
    val divBy: Long,
    val test: (Long) -> Boolean,
    val success: Int, // if true: throw to
    val fail: Int, // if false: trow to
    var inspectCounter: Long = 0,
)

fun getMonkeys(input: List<String>): List<Monkey> = input
    .chunked(7)
    .map { m ->
        val items = m[1].substringAfter(": ").split(", ").map { it.toLong() }
        val ops = m[2].substringAfter("old ").split(" ")
        val test = m[3].substringAfter("by ").toInt()

        Monkey(
            name = m[0].substringAfter(" ")[0].digitToInt(),
            items = items.toMutableList(),
            divBy = test.toLong(),
            op = { old ->
                val item = if (ops[1] == "old") old else ops[1].toLong()
                when (ops[0]) {
                    "+" -> (old + item)
                    "*" -> old * item
                    else -> error("wtf???")
                }
            },
            test = { item -> item % test == 0L },
            success = m[4].substringAfter("monkey ").toInt(),
            fail = m[5].substringAfter("monkey ").toInt(),
        )
    }

fun inspection(monkeys: List<Monkey>, divider: Long, monkeyMod: Long? = null) {
    monkeys.forEach { monkey ->
        monkey.inspectCounter += monkey.items.size
        monkey.items.forEach { item ->
            var worry = monkey.op(item) / divider
            monkeyMod?.let { worry %= monkeyMod }
            val test = monkey.test(worry)
            if (test) monkeys[monkey.success].items += worry
            else monkeys[monkey.fail].items += worry
        }
        monkey.items.clear()
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = getMonkeys(input)
        repeat(20) { inspection(monkeys, 3) }

        val (a, b) = monkeys.sortedByDescending { m -> m.inspectCounter }
        return a.inspectCounter * b.inspectCounter
    }

    fun part2(input: List<String>): Long {
        val monkeys = getMonkeys(input)

        val monkeyMod = monkeys
            .map { m -> m.divBy }
            .reduce { a, b -> a * b }

        repeat(10_000) { inspection(monkeys, 1, monkeyMod) }

        val (a, b) = monkeys.sortedByDescending { m -> m.inspectCounter }
        return a.inspectCounter * b.inspectCounter
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    check(part1(testInput) == 10605L)
    println(part1(input))

    check(part2(testInput) == 2713310158L)
    println(part2(input))
}
