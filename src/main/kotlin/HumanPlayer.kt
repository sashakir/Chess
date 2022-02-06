import java.util.regex.Pattern

class HumanPlayer : Player {
    override val name = "Human"

    private fun validateMove(field: Field, from: Point, to: Point, color: Int) = when {
        field[from] == null -> "From field is empty!"
        field[from]!!.color != color -> "Not your color!"
        to !in field[from]!!.possibleMoves(field, from) -> "Move is impossible!"
        else -> null
    }

    override fun nextMove(field: Field, color: Int): Pair<Point, Point>? {
        while (true) {
            println("Enter next move:")
            val input = readLine()!!.trim().toLowerCase()
            if (input == "undo")
                return null
            val matcher = Pattern.compile("([a-h])([1-8])-([a-h])([1-8])").matcher(input)
            if (!matcher.matches()) {
                println("Incorrect format. You're stupid pidoras")
                continue
            }
            val from = Point(7 - (matcher.group(2).toCharArray()[0] - '1'), matcher.group(1).toCharArray()[0] - 'a')
            val to = Point(7 - (matcher.group(4).toCharArray()[0] - '1'), matcher.group(3).toCharArray()[0] - 'a')
            val result = validateMove(field, from, to, color)
            if (result != null) {
                println(result)
                continue
            }

            return Pair (from, to)
        }
    }
}
