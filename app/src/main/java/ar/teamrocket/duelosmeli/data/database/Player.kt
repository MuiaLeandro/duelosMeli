package ar.teamrocket.duelosmeli.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity (tableName = "Players")
data class Player (
    @ColumnInfo (name="name")
    var name:String,

    @ColumnInfo (name="score")
    var score:Int
){
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    var id:Long = 0
}

@Entity (tableName = "Multiplayers")
data class Multiplayer (
    @ColumnInfo (name="name")
    var name:String,

    @ColumnInfo (name="score")
    var score:Int
){
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    var id:Long = 0
}