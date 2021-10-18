package ar.teamrocket.duelosmeli.data.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Player::class,Multiplayer::class],
    version = 1
)
abstract class DuelosMeliDb: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
}