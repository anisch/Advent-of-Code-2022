fun getSignalStrength(cycles: Int, registerX: Int) =
    if (cycles  % 40 == 20) cycles * registerX
    else 0

fun main() {
    fun part1(input: List<String>): Int {
        var cycles = 0
        var regX = 1
        var signalSum = 0

        input.forEach { ins ->
            if (ins == "noop") {
                signalSum += getSignalStrength(++cycles, regX)
            } else {
                signalSum += getSignalStrength(++cycles, regX)
                signalSum += getSignalStrength(++cycles, regX)
                regX += ins.split(" ")[1].toInt()
            }
        }
        return signalSum
    }

    fun part2(input: List<String>): String {
        val crt = StringBuilder()
        var cycles = 0
        var regX = 1

        input.forEach { ins ->
            val sprite = regX - 1..regX + 1
            if (ins == "noop") {
                crt.append(if (cycles++ % 40 in sprite) "#" else ".")
            } else {
                crt.append(if (cycles++ % 40 in sprite) "#" else ".")
                crt.append(if (cycles++ % 40 in sprite) "#" else ".")
                regX += ins.split(" ")[1].toInt()
            }
        }
        return crt.chunked(40).joinToString("\n") { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    check(part1(testInput) == 13140)
    println(part1(input))

    val testResultPart2 = """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()

    check(part2(testInput) == testResultPart2)
    println(part2(input))
}
