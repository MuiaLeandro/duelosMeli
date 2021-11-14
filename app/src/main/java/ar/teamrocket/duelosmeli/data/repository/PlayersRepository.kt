package ar.teamrocket.duelosmeli.data.repository

import android.app.Application
import android.content.Context
import androidx.room.Room
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.database.PlayerDao


interface PlayersRepository {

    suspend fun getAllMultiplayers(): List<Multiplayer>

    fun deleteAllMultiplayer(multiplayers: List<Multiplayer>)

    suspend fun deleteMultiplayer(multiplayers: Multiplayer)

    suspend fun insertMultiplayer(multiplayer: Multiplayer)

    suspend fun getAllMultiplayersId(): List<Long>

    suspend fun getAllMultiplayersOrderByScore(): List<Multiplayer>

    suspend fun updateMultiplayer(multiplayer: Multiplayer)
}