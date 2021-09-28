package ar.teamrocket.duelosmeli.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Player::class],
    version = 1
)
abstract class DuelosMeliDb: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
}