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


    ///////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultiplayer(multiplayer: Multiplayer)

    @Query("SELECT * FROM multiplayers")
    suspend fun getAllMultiplayer(): List<Multiplayer>

    @Delete
    suspend fun deleteMultiplayer(multiplayer: Multiplayer)

    @Delete
    fun deleteAllMultiplayers(multiplayers: List<Multiplayer>)

    @Query("SELECT * FROM multiplayers ORDER BY score DESC")
    suspend fun getAllMultiplayerOrderByScore(): List<Multiplayer>

    @Query("SELECT id FROM multiplayers")
    suspend fun getAllMultiplayerId(): List<Long>

    @Update
    suspend fun updateMultiplayer(multiplayer: Multiplayer)

    @Update
    suspend fun updateMultiplayers(multiplayers: List<Multiplayer>)

}