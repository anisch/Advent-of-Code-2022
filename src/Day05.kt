val regexOps = """move \d from \d to \d""".toRegex()

data class Ops(val move: Int, val from: Int, val to: Int)

fun createStack(list: List<String>): MutableMap<Int, MutableList<Char>> {
    val stack = mutableMapOf<Int, MutableList<Char>>()

    list
        .takeWhile { it.isNotBlank() }
        .dropLast(1)
        .map { it.chunked(4) }
        .map { it.map { box -> box[1] } }
        .forEach {
            it.forEachIndexed { idx, box ->
                if (!box.isWhitespace()) {
                    stack[idx + 1]?.add(0, box) ?: mutableListOf(box)
                }
            }
        }

    return stack
}

fun createOps(list: List<String>): List<Ops> = list
    .asSequence()
    .dropWhile { !regexOps.matches(it) }
    .map { it.replace("move ", "") }
    .map { it.replace("from", "") }
    .map { it.replace("to", "") }
    .map { it.split("  ") }
    .map { Ops(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
    .toList()

fun MutableList<*>.removeLast(i: Int) = repeat((1..i).count()) { removeAt(lastIndex) }

fun MutableMap<Int, MutableList<Char>>.topOfStackAsString() = toSortedMap()
    .mapValues { it.value.last().toString() }
    .values
    .reduce { acc, s -> acc + s }

fun main() {
    fun part1(input: List<String>): String {
        val stack = createStack(input)
        val ops = createOps(input)

        ops.forEach { op ->
            val size = stack[op.from]!!.size
            val tmp = stack[op.from]!!.drop(size - op.move)
            stack[op.to]!!.addAll(tmp.reversed())
            stack[op.from]!!.removeLast(op.move)
        }

        return stack.topOfStackAsString()
    }

    fun part2(input: List<String>): String {
        val stack = createStack(input)
        val ops = createOps(input)

        ops.forEach { op ->
            val size = stack[op.from]!!.size
            val tmp = stack[op.from]!!.drop(size - op.move)
            stack[op.to]!!.addAll(tmp)
            stack[op.from]!!.removeLast(op.move)
        }

        return stack.topOfStackAsString()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    check(part1(testInput) == "CMZ")
    println(part1(input))

    check(part2(testInput) == "MCD")
    println(part2(input))
}
