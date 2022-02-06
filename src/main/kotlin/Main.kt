class FigureType(
    val symbol: List<Char>, //For white and black
    val allowedMoves: List<Delta>,
    val multiMove: Boolean,
    val weight: Int
)

val pawn = FigureType(
    listOf('\u2659', '\u265F'),
    listOf(Delta(1, 0), Delta(1, -1), Delta(1, 1)),
    false,
    1
)
val knight = FigureType(
    listOf('\u2658', '\u265E'),
    listOf(
        Delta(1, 2), Delta(-1, 2), Delta(1, -2), Delta(-1, -2),
        Delta(2, 1), Delta(2, -1), Delta(-2, 1), Delta(-2, -1)

    ),
    false,
    3
)

val bishop = FigureType(
    listOf('\u2657', '\u265D'),
    listOf(Delta(1, 1), Delta(-1, 1), Delta(1, -1), Delta(-1, -1)),
    true,
    3
)

val rook = FigureType(
    listOf('\u2656', '\u265C'),
    listOf(Delta(0, 1), Delta(0, -1), Delta(1, 0), Delta(-1, 0)),
    true,
    5
)

val queen = FigureType(                             // ferz'
    listOf('\u2655', '\u265B'),
    listOf(
        Delta(0, 1), Delta(0, -1), Delta(1, 0), Delta(-1, 0),
        Delta(1, 1), Delta(-1, 1), Delta(1, -1), Delta(-1, -1)
    ),
    true,
    8
)

val king = FigureType(                             // korol'
    listOf('\u2654', '\u265A'),
    listOf(
        Delta(0, 1), Delta(0, -1), Delta(1, 0), Delta(-1, 0),
        Delta(1, 1), Delta(-1, 1), Delta(1, -1), Delta(-1, -1)
    ),
    false,
    1000
)

data class Point(val x: Int, val y: Int) {
    operator fun plus(d: Delta) = Point(x + d.dx, y + d.dy)

    override fun toString() = "${'A' + y}${'1' + (7 - x)}"
}

data class Delta(val dx: Int, val dy: Int) {
    operator fun times(k: Int) = Delta(dx * k, dy * k)
}

fun main(args: Array<String>) {
    var field = Field.initialArrangement()
    val history = mutableListOf(field)
    println(field)
    var color = 0
    val colors = listOf("White", "Black")
//    val players = listOf(HumanPlayer(), RandomPlayer())
    val players = listOf(HumanPlayer(), BruteForcePlayer(5))

    while (true) {
        println("${colors[color]} (${players[color].name}) moves: ")
        val nextMove = players[color].nextMove(field, color)

        if (nextMove == null) { //Undo
            if (history.size > 2) {
                history.removeAt(history.size - 1)
                history.removeAt(history.size - 1)
                field = history[history.size - 1]
                println(field)
            }

            continue
        }
        val (from, to) = nextMove
        print("$from-$to")
        field[to]?.let {
            print("  ${it.type.symbol[it.color]} eaten!")
        }
        field = field.move(from, to)

        if (field.getAllMoves(color).any { field[it.second].let { it != null && it.type == king }})
            print(" check!!!")
        
        println()
        history += field
        println(field)
        println("price: (white: ${field.price(0)}, black: ${field.price(1)})\n")
        color = 1 - color
    }
}


