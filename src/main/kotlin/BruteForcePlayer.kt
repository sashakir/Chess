import java.util.stream.Collectors

class BruteForcePlayer(val maxDepth: Int): Player {
    override val name = "Computer (brute force depth $maxDepth)"

    private fun traverse(field: Field, color: Int, depth: Int, curMaxDepth: Int): Triple<Point?, Point?, Int> {
        if (depth == maxDepth)
            return Triple(null, null, field.price(color))

        val moves = field.getAllMoves(color).parallelMap(depth == 0) { (from, to) ->
            Triple(from, to, -traverse(field.move(from, to), 1 - color, depth + 1, curMaxDepth).third)
        }
        val max = moves.maxOf { it.third }
        val bestMoves = moves.filter { it.third == max }

        if (bestMoves.size == 1)
            return bestMoves.first()

        return bestMoves.maxByOrNull { field.move(it.first, it.second).price2(color) }!!
    }

    override fun nextMove(field: Field, color: Int): Pair<Point, Point> {
        for (curMaxDepth in maxDepth downTo 2) {
            val (from, to, price) = traverse(field, color, 0, curMaxDepth)

            if (price > -king.weight / 2 || curMaxDepth == 2) {
                if (price < -king.weight / 2)
                    println("Checkmate :((")

                return Pair(from!!, to!!)
            }
        }

        throw IllegalStateException()
    }
}

fun <T, U>List<T>.parallelMap(isParallel: Boolean, f: (T) -> U) : List<U> {
    return if (isParallel) parallelStream().map(f).collect(Collectors.toList()) else map(f)
}