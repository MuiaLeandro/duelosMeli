package ar.teamrocket.duelosmeli.domain

class Game(idPlayer: Long){
    var playerId: Long = idPlayer
    var state: Boolean = true
    var points: Int = 0
    var errors: Int = 0

    fun pointsCounter(game: Game){
        game.points++
    }

    fun errorsCounter(game: Game) {
        game.errors++
    }

    fun errorsDiscounter(game: Game) {
        game.errors--
    }
}
