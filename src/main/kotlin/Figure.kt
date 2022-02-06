class Figure(
    val type: FigureType,
    val color: Int
) {
    fun possibleMoves(field: Field, position: Point): List<Point> {
        val res = mutableListOf<Point>()

        val allowedDirections = type.allowedMoves
            .map { true }
            .toMutableList()

        fun isValidPos(pos: Point) = pos.x in 0..7 && pos.y in 0..7

        for (i in 1..if (type.multiMove) 8 else 1) {
            val nextMoves = type.allowedMoves.withIndex()
                .filter { (index, _) -> allowedDirections[index] }
                .map { (index, dir) -> Pair(index, position + (if (type == pawn && color == 0) dir * -1 else dir) * i) }
                .filter { (_, pos) -> isValidPos(pos) }

            nextMoves.forEach { (index, pos) ->
                if (field[pos] != null)
                    allowedDirections[index] = false
            }

            res += nextMoves.filter { (index, pos) ->
                val dest = field[pos]
                val eaten = dest != null && dest.color != color
                (dest == null || eaten) && (type != pawn || ((type.allowedMoves[index].dy == 0) != eaten))
            }.map { (_, pos) -> pos }
        }
        if (type == pawn && color == 0 && position.x == 6 && field[Point(5, position.y)] == null && field[Point(4, position.y)] == null)
            res += Point(4, position.y)
        if (type == pawn && color == 1 && position.x == 1 && field[Point(2, position.y)] == null && field[Point(3, position.y)] == null)
            res += Point(3, position.y)

        return res
    }
}
