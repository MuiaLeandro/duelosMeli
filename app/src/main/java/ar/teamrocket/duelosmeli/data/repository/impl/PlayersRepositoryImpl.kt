package ar.teamrocket.duelosmeli.data.repository.impl

import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository

class PlayersRepositoryImpl (val playerDao : PlayerDao): PlayersRepository {

    override suspend fun getAllMultiplayers(): List<Multiplayer> {
        return playerDao.getAllMultiplayer()
    }

    override fun deleteAllMultiplayer(multiplayers: List<Multiplayer>) {
        playerDao.deleteAllMultiplayers(multiplayers)
    }
    override suspend fun deleteMultiplayer(multiplayers: Multiplayer) {
        playerDao.deleteMultiplayer(multiplayers)
    }
    override suspend fun insertMultiplayer(multiplayer: Multiplayer){
        playerDao.insertMultiplayer(multiplayer)
    }

    override suspend fun getAllMultiplayersId(): List<Long> {
        return playerDao.getAllMultiplayerId()
    }

    override suspend fun getAllMultiplayersOrderByScore(): List<Multiplayer> {
        return playerDao.getAllMultiplayerOrderByScore()
    }
    override suspend fun updateMultiplayer(multiplayer: Multiplayer){
        playerDao.updateMultiplayer(multiplayer)
    }
    override suspend fun updateMultiplayers(multiplayers: List<Multiplayer>) {
        playerDao.updateMultiplayers(multiplayers)
    }
}