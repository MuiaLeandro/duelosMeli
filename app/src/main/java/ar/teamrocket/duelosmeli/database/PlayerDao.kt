package ar.teamrocket.duelosmeli.database

import androidx.room.*
import ar.teamrocket.duelosmeli.database.Player

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAll(): List<Player>

    @Insert
    fun insertPlayer(player: Player)

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)
}