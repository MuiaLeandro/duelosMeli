package ar.teamrocket.duelosmeli.database

import androidx.room.*

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAll(): List<Player>

    @Query("SELECT * FROM players WHERE id = :id")
    fun getById(id: Long): Player

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlayer(player: Player)

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)
}