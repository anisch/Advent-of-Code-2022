data class File(val name: String, val size: Long = 0)

class Directory {
    var name: String = ""
    var upDir: Directory? = null
    val listDirs = mutableListOf<Directory>()
    val listFiles = mutableListOf<File>()
    var totalSize = 0L
}

fun calculateSizes(root: Directory): Directory {
    if (root.listDirs.isNotEmpty()) {
        root.totalSize += calculateSizes(root.listDirs)
    }
    return root
}

fun calculateSizes(list: List<Directory>): Long = list.map { calculateSizes(it) }.sumOf { it.totalSize }

fun tree(root: Directory): List<Directory> =
    if (root.listDirs.isNotEmpty()) listOf(root) + tree(root.listDirs)
    else listOf(root)

fun tree(list: List<Directory>): List<Directory> = list.flatMap { tree(it) }

fun createFileSystem(input: List<String>): Directory {
    val fileSystem = Directory().also { it.name = "/" }
    var current = fileSystem

    input
        .drop(1) // jump over "cd /"
        .forEach { line ->
            when {
                line.startsWith("$ cd") -> {
                    val s = line.split(" ")
                    val tmp =
                        if (s[2] == "..") current.upDir!!
                        else current.listDirs.first { f -> f.name == s[2] }
                    current = tmp
                }

                line.startsWith("$ ls") -> {} // ignore

                else -> { // ls
                    val (size, name) = line.split(" ")
                    if (line.startsWith("dir")) {
                        val dir = Directory().also {
                            it.name = name
                            it.upDir = current
                        }
                        current.listDirs += dir
                    } else {
                        current.listFiles += File(name, size.toLong())
                        current.totalSize += size.toLong()
                    }
                }
            }
        }

    return calculateSizes(fileSystem)
}

fun main() {
    fun part1(input: List<String>): Long {
        val fileSystem = createFileSystem(input)

        return tree(fileSystem)
            .filter { it.totalSize <= 100_000 }
            .sumOf { it.totalSize }
    }

    fun part2(input: List<String>): Long {
        val maxSpace = 70_000_000L
        val minSpace = 30_000_000L

        val fileSystem = createFileSystem(input)
        val free = maxSpace - fileSystem.totalSize
        val freeUp = minSpace - free

        return tree(fileSystem)
            .filter { it.totalSize >= freeUp }
            .minOf { it.totalSize }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    check(part1(testInput) == 95437L)
    println(part1(input))

    check(part2(testInput) == 24933642L)
    println(part2(input))
}
