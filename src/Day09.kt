import kotlin.math.abs

private fun Vec.isTouching(o: Vec): Boolean =
    abs(x - o.x) <= 1 && abs(y - o.y) <= 1

private fun Vec.move(dir: String): Vec = when (dir) {
    "U" -> Vec(x, y + 1)
    "D" -> Vec(x, y - 1)
    "L" -> Vec(x - 1, y)
    "R" -> Vec(x + 1, y)
    else -> error("wtf???")
}

fun main() {
    fun part1(input: List<String>): Int {
        var head = Vec(0, 0)
        val tails = mutableListOf(head)

        input
            .map { it.split(" ") }
            .forEach { (dir, steps) ->
                for (step in 0 until steps.toInt()) {
                    val tmp = head
                    head = tmp.move(dir)

                    if (tails[tails.lastIndex].isTouching(head)) continue
                    tails += tmp
                }
            }

        return tails.toSet().size
    }

    fun part2(input: List<String>): Int {
        val rope = Array(10) { Vec(0, 0) }
        val tails = mutableListOf(rope[rope.lastIndex])

        input
            .map { it.split(" ") }
            .forEach { (dir, steps) ->
                (0 until steps.toInt()).forEach { _ ->
                    val tmp = rope[0]
                    rope[0] = tmp.move(dir)

                    for (rdx in 0 until 9) {
                        val head = rope[rdx]
                        val tail = rope[rdx + 1]

                        if (tail.isTouching(head)) continue

                        val offset = head - tail
                        val normalized = Vec(
                            offset.x.coerceIn(-1..1),
                            offset.y.coerceIn(-1..1),
                        )
                        rope[rdx + 1] = tail + normalized
                    }
                    tails += rope[rope.lastIndex]
                }
            }

        return tails.toSet().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day09_test_part1")
    val testInput2 = readInput("Day09_test_part2")
    val input = readInput("Day09")

    check(part1(testInput1) == 13)
    println(part1(input))

    check(part2(testInput2) == 36)
    println(part2(input)) // 2607
}
