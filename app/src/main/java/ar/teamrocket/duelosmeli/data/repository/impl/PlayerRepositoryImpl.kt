package ar.teamrocket.duelosmeli.data.repository.impl

import ar.teamrocket.duelosmeli.data.database.Player
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.data.repository.PlayerRepository

class PlayerRepositoryImpl (private val playerDao : PlayerDao): PlayerRepository {
    override suspend fun getAllPlayers(): List<Player> {
        return playerDao.getAll()
    }

    override suspend fun insertPlayer(player: Player) {
        return playerDao.insertPlayer(player)
    }

    override suspend fun getTopTenPlayersOrderByScore(): List<Player> {
        return playerDao.getTopTenOrderByScore()
    }

    override suspend fun updatePlayer(player: Player) {
        return playerDao.updatePlayer(player)
    }

    override suspend fun getPlayerByName(name: String): List<Player> {
        return playerDao.getByName(name)
    }

    override suspend fun getPlayerById(id: Long): List<Player> {
        return playerDao.getById(id)
    }

    override suspend fun getAllPlayerOrderByName(): List<Player> {
        return playerDao.getTopTenOrderByScore()
    }
}