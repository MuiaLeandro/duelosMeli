package ar.teamrocket.duelosmeli.data.repository

import ar.teamrocket.duelosmeli.data.database.Multiplayer

interface PlayersRepository {

    suspend fun getAllMultiplayers(): List<Multiplayer>

    fun deleteAllMultiplayer(multiplayers: List<Multiplayer>)

    suspend fun deleteMultiplayer(multiplayers: Multiplayer)

    suspend fun insertMultiplayer(multiplayer: Multiplayer)

    suspend fun getAllMultiplayersId(): List<Long>

    suspend fun getAllMultiplayersOrderByScore(): List<Multiplayer>

    suspend fun updateMultiplayer(multiplayer: Multiplayer)

    suspend fun updateMultiplayers(multiplayers: List<Multiplayer>)
}
