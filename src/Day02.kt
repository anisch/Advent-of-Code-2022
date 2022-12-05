enum class Shape(val score: Int) { ROCK(1), PAPER(2), SCISSORS(3)}
enum class OutCome(val result: Int) { LOST(0), DRAW(3), WIN(6)}

fun parseShapes(line: String): Pair<Shape, Shape> {
    val s = line
        .split(" ")
        .map { s -> parseShape(s) }
    return Pair(s[0], s[1])
}

fun parseShapeOutCome(line: String): Pair<Shape, OutCome> {
    val s = line.split(" ")
    return Pair(parseShape(s[0]), parseOutCome(s[1]))
}

fun parseShape(s: String): Shape = when (s) {
    "A", "X" -> Shape.ROCK
    "B", "Y" -> Shape.PAPER
    "C", "Z" -> Shape.SCISSORS
    else -> error("wtf???")
}

fun parseOutCome(s: String) = when (s) {
    "X" -> OutCome.LOST
    "Y" -> OutCome.DRAW
    "Z" -> OutCome.WIN
    else -> error("wtf???")
}

fun getShapeOutCome(s: Shape, o: OutCome): Shape = when (o) {
    OutCome.WIN -> when (s) {
        Shape.ROCK -> Shape.PAPER
        Shape.PAPER -> Shape.SCISSORS
        Shape.SCISSORS -> Shape.ROCK
    }

    OutCome.LOST -> when (s) {
        Shape.ROCK -> Shape.SCISSORS
        Shape.PAPER -> Shape.ROCK
        Shape.SCISSORS -> Shape.PAPER
    }

    else -> s
}

fun getResult(m: Pair<Shape, Shape>): Pair<OutCome, OutCome> = when {
    m.first == Shape.ROCK && m.second == Shape.SCISSORS -> Pair(OutCome.WIN, OutCome.LOST)
    m.first == Shape.ROCK && m.second == Shape.PAPER -> Pair(OutCome.LOST, OutCome.WIN)
    m.first == Shape.PAPER && m.second == Shape.ROCK -> Pair(OutCome.WIN, OutCome.LOST)
    m.first == Shape.PAPER && m.second == Shape.SCISSORS -> Pair(OutCome.LOST, OutCome.WIN)
    m.first == Shape.SCISSORS && m.second == Shape.PAPER -> Pair(OutCome.WIN, OutCome.LOST)
    m.first == Shape.SCISSORS && m.second == Shape.ROCK -> Pair(OutCome.LOST, OutCome.WIN)
    else -> Pair(OutCome.DRAW, OutCome.DRAW)
}

fun getScore(m: Pair<Shape, Shape>, o: Pair<OutCome, OutCome>): Pair<Int, Int> =
    Pair(m.first.score + o.first.result, m.second.score + o.second.result)

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { parseShapes(it) }
            .map { getScore(it, getResult(it)) }
            .reduce { acc, next -> Pair(acc.first + next.first, acc.second + next.second) }
            .second
    }

    fun part2(input: List<String>): Int {
        return input
            .map { parseShapeOutCome(it) }
            .map { Pair(it.first, getShapeOutCome(it.first, it.second)) }
            .map { getScore(it, getResult(it)) }
            .reduce { acc, next -> Pair(acc.first + next.first, acc.second + next.second) }
            .second
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val input = readInput("Day02")

    check(part1(testInput) == 15)
    println(part1(input))

    check(part2(testInput) == 12)
    println(part2(input))
}
