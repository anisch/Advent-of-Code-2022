import kotlin.math.abs

enum class Job { TIMES, PLUS, MINUS, DIV }

open class MonkeyJob(val name: String)

class MonkeyNumber(
    name: String,
    var number: Long,
) : MonkeyJob(name)

class MonkeyOperation(
    name: String,
    val a: String,
    val b: String,
    val job: Job,
) : MonkeyJob(name)

fun getMonkeyJobs(input: List<String>) = input
    .map { line ->
        val name = line.substringBefore(":")
        if (line.matches("[a-zA-Z]{4}: [a-zA-Z]{4} [-+*/] [a-zA-Z]{4}".toRegex())) {
            val op = line.substringAfter(": ").split(" ")
            val job = when (op[1]) {
                "*" -> Job.TIMES
                "/" -> Job.DIV
                "+" -> Job.PLUS
                "-" -> Job.MINUS
                else -> error("wtf???")
            }
            MonkeyOperation(name, op[0], op[2], job)
        } else {
            MonkeyNumber(name, line.substringAfter(": ").toLong())
        }
    }

fun doJob(monkeyJob: List<MonkeyJob>, node: MonkeyJob?): Long = when (node) {
    null -> 0
    is MonkeyNumber -> node.number
    is MonkeyOperation -> {
        val ma = monkeyJob.find { it.name == node.a }
        val mb = monkeyJob.find { it.name == node.b }
        when (node.job) {
            Job.PLUS -> doJob(monkeyJob, ma) + doJob(monkeyJob, mb)
            Job.MINUS -> doJob(monkeyJob, ma) - doJob(monkeyJob, mb)
            Job.TIMES -> doJob(monkeyJob, ma) * doJob(monkeyJob, mb)
            Job.DIV -> doJob(monkeyJob, ma) / doJob(monkeyJob, mb)
        }
    }

    else -> error("wtf???")
}

fun main() {
    fun part1(input: List<String>): Long {
        val monkeyJobs = getMonkeyJobs(input)
        val root = monkeyJobs.find { it.name == "root" }

        return doJob(monkeyJobs, root)
    }

    fun part2(input: List<String>): Long {
        val monkeyJobs = getMonkeyJobs(input)
        val root = monkeyJobs.find { it.name == "root" } as MonkeyOperation
        val rootA = monkeyJobs.find { it.name == root.a }
        val rootB = monkeyJobs.find { it.name == root.b }

        val human = monkeyJobs.find { it.name == "humn" } as MonkeyNumber

        do {
            val sumA = doJob(monkeyJobs, rootA)
            val sumB = doJob(monkeyJobs, rootB)

            val isNotEqual = sumA != sumB
            if (isNotEqual) {
                // simple bruteforce
                val diff = abs(sumA - sumB)
                when {
                    diff <= 10_000 -> human.number++
                    diff <= 100_000 -> human.number += 100
                    diff <= 1_000_000 -> human.number += 1_000
                    diff <= 10_000_000 -> human.number += 10_000
                    diff <= 100_000_000 -> human.number += 100_000
                    diff <= 1_000_000_000 -> human.number += 1_000_000
                    diff <= 10_000_000_000 -> human.number += 10_000_000
                    diff <= 100_000_000_000 -> human.number += 100_000_000
                    diff <= 1_000_000_000_000 -> human.number += 1_000_000_000
                    else -> human.number += 10_000_000_000
                }
            }
        } while (isNotEqual)

        return human.number
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    val input = readInput("Day21")

    check(part1(testInput) == 152L)
    println(part1(input))

    check(part2(testInput) == 301L)
    println(part2(input))
}
