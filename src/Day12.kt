private typealias Area = Array<CharArray>

private fun Area.neighbors(c: Vec): List<Vec> {
    val next = mutableListOf<Vec>()
    val cc = when {
        this[c.y][c.x] == 'S' -> 'a'
        this[c.y][c.x] == 'E' -> 'z'
        else -> this[c.y][c.x]
    }

    if (0 < c.x) {
        val cl = this[c.y][c.x - 1]
        if (cl <= cc + 1) {
            next += Vec(c.x - 1, c.y)
        }
    }

    if (c.x < this[0].lastIndex) {
        val cr = this[c.y][c.x + 1]
        if (cr <= cc + 1) {
            next += Vec(c.x + 1, c.y)
        }
    }

    if (c.y < lastIndex) {
        val cb = this[c.y + 1][c.x]
        if (cb <= cc + 1) {
            next += Vec(c.x, c.y + 1)
        }
    }

    if (0 < c.y) {
        val ct = this[c.y - 1][c.x]
        if (ct <= cc + 1) {
            next += Vec(c.x, c.y - 1)
        }
    }

    return next
}

fun main() {
    fun part1(input: List<String>): Int {
        val area = input
            .map { it.toCharArray() }
            .toTypedArray()

        var start = Vec(0, 0)
        var goal = Vec(0, 0)

        for (y in area.indices) {
            for (x in area[0].indices) {
                if (area[y][x] == 'S') {
                    area[y][x] = 'a'
                    start = Vec(x, y)
                }
                if (area[y][x] == 'E') {
                    area[y][x] = 'z'
                    goal = Vec(x, y)
                }
            }
        }

        val frontier = ArrayDeque<Vec>()
        frontier.addFirst(start)

        val cameFrom = mutableMapOf<Vec, Vec?>()
        cameFrom[start] = null

        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()

            if (current == goal) break
            for (next in area.neighbors(current)) {
                if (!cameFrom.containsKey(next)) {
                    frontier.add(next)
                    cameFrom[next] = current
                }
            }
        }

        var current = goal
        val path = mutableListOf<Vec>()
        while (current != start) {
            path += current
            current = cameFrom[current]!!
        }

        return path.size
    }

    fun part2(input: List<String>): Int {
        val area = input
            .map { it.toCharArray() }
            .toTypedArray()

        val startVecs = mutableListOf<Vec>()
        var goal = Vec(0, 0)

        for (y in area.indices) {
            for (x in area[0].indices) {
                if (area[y][x] in listOf('S', 'a')) {
                    area[y][x] = 'a'
                    startVecs += Vec(x, y)
                }
                if (area[y][x] == 'E') {
                    area[y][x] = 'z'
                    goal = Vec(x, y)
                }
            }
        }

        val min = startVecs.minOf { start ->
            val frontier = ArrayDeque<Vec>()
            frontier.addFirst(start)

            val cameFrom = mutableMapOf<Vec, Vec?>()
            cameFrom[start] = null

            while (frontier.isNotEmpty()) {
                val current = frontier.removeFirst()

                if (current == goal) break
                for (next in area.neighbors(current)) {
                    if (!cameFrom.containsKey(next)) {
                        frontier.add(next)
                        cameFrom[next] = current
                    }
                }
            }

            var current = goal
            val path = mutableListOf<Vec>()
            while (current != start) {
                path += current
                val vec = cameFrom[current]
                if (vec != null) current = vec
                else return@minOf Int.MAX_VALUE
            }
            path.size
        }

        return min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    val input = readInput("Day12")

    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}
