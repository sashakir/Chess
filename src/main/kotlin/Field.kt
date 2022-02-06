class Field(private val field: Array<Array<Figure?>> = Array(8) { Array(8) { null } }) {
    operator fun get(point: Point) = field[point.x][point.y]
    operator fun set(point: Point, figure: Figure?) {
        field[point.x][point.y] = figure
    }

    fun move(from: Point, to: Point): Field {
        val newField = Field(Array(8) { i -> Array(8) { j -> field[i][j] } })
        newField[to] = newField[from]
        newField[from] = null
        return newField
    }

    fun getAllFigures(color: Int) = field.indices.
        flatMap { i -> field[i].indices.map { j -> field[i][j]?.let { if (it.color == color) Pair(Point(i, j), it) else null }}.filterNotNull() }

    fun getAllMoves(color: Int) = getAllFigures(color).
        flatMap { (source, figure) -> figure.possibleMoves(this, source).map { dest -> Pair(source, dest) } }

    override fun toString(): String {
        val result = StringBuilder()
        for (i in field.indices) {
            result.append("${8 - i} ")
            for (j in field[0].indices) {
                val figure = field[i][j]
                val symbol = if (figure != null) figure.type.symbol[figure.color] else ' '
                result.append("| $symbol ")
            }

            result.append("|\n")
        }
        result.append(" ")
        for (i in field.indices) {
            result.append(" | ${'A' + i}")
        }
        result.append(" |\n")
        return result.toString()
    }

    fun price(color: Int) = getAllFigures(color).sumOf { it.second.type.weight } -
            getAllFigures(1 - color).sumOf { it.second.type.weight }
    fun price2(color: Int) = getAllFigures(color).sumOf { if (color == 1) it.first.x else 7 - it.first.x } -
            getAllFigures(1 - color).sumOf { if (color == 0) it.first.x else 7 - it.first.x }

    companion object {
        fun initialArrangement(): Field {
            val firstRow = listOf(rook, knight, bishop, queen, king, bishop, knight, rook)
            val field = Field()
            for (i in 0..7) {
                fun putFigure(pos: Point, type: FigureType, color: Int) {
                    field[pos] = Figure(type, color)
                }

                putFigure(Point(1, i), pawn, 1)
                putFigure(Point(6, i), pawn, 0)
                putFigure(Point(0, i), firstRow[i], 1)
                putFigure(Point(7, i), firstRow[i], 0)
            }

            return field
        }
    }
}
