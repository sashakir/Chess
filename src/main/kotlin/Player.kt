interface Player {
    val name: String

    fun nextMove(field: Field, color: Int): Pair<Point, Point>?
}
