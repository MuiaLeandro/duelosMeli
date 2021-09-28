package ar.teamrocket.duelosmeli.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "Players")
data class Player (
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    var id:Long,

    @ColumnInfo (name="name")
    var name:String,

    @ColumnInfo (name="score")
    var score:Int
)