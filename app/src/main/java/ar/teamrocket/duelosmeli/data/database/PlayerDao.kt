package ar.teamrocket.duelosmeli.data.database

import androidx.room.*

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAll(): List<Player>

    @Query("SELECT * FROM players ORDER BY score DESC LIMIT 10")
    fun getTopTenOrderByScore(): List<Player>

    @Query("SELECT * FROM players ORDER BY name")
    fun getAllOrderByName(): List<Player>

    @Query("SELECT * FROM players WHERE id = :id")
    fun getById(id: Long): List<Player>

    @Query("SELECT * FROM players WHERE name = :name")
    fun getByName(name: String): List<Player>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlayer(player: Player)

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)
}