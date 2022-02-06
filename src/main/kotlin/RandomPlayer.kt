import kotlin.random.Random

class RandomPlayer: Player {
    override val name = "Computer (random)"

    override fun nextMove(field: Field, color: Int) = field.getAllMoves(color).let { it[Random.nextInt(it.size)] }
}