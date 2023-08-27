package ar.teamrocket.duelosmeli.data.repository

import ar.teamrocket.duelosmeli.data.database.Player

interface PlayerRepository {
    suspend fun getAllPlayers(): List<Player>
    suspend fun insertPlayer(player: Player)
    suspend fun getTopTenPlayersOrderByScore(): List<Player>
    suspend fun updatePlayer(player: Player)
    suspend fun getPlayerByName(name: String): List<Player>
    suspend fun getPlayerById(id: Long): List<Player>
    suspend fun getAllPlayerOrderByName(): List<Player>
}
