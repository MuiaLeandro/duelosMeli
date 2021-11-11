package ar.teamrocket.duelosmeli.data.repository

import android.app.Application
import android.content.Context
import androidx.room.Room
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.database.PlayerDao

class PlayersRepository(application: Application) {
    private lateinit var database: DuelosMeliDb
    private lateinit var playerDao: PlayerDao

    init {
        injectDependencies(application.applicationContext)
    }

    companion object {
        private fun buildDatabase(context: Context): DuelosMeliDb {
            return Room.databaseBuilder(
                context,
                DuelosMeliDb::class.java,
                "duelosmeli-db"
            ).build()
        }
    }

    /**
     * Manual dependency injection
     */
    private fun injectDependencies(context: Context) {
        this.database = buildDatabase(context)      //No es una buena práctica pasar el contexto de su actividad al modelo de vista de la actividad, ya que es una pérdida de memoria.
                                                    //Por lo tanto, para obtener el contexto en su ViewModel, la clase ViewModel debería extender la clase Android View Model
        this.playerDao = this.database.playerDao()
    }

    suspend fun getAllMultiplayers(): List<Multiplayer> {
        return playerDao.getAllMultiplayer()
    }

    fun deleteAllMultiplayer(multiplayers: List<Multiplayer>) {
        playerDao.deleteAllMultiplayers(multiplayers)
    }
    suspend fun deleteMultiplayer(multiplayers: Multiplayer) {
        playerDao.deleteMultiplayer(multiplayers)
    }
     suspend fun insertMultiplayer(multiplayer: Multiplayer){
        playerDao.insertMultiplayer(multiplayer)
    }

    suspend fun getAllMultiplayersId(): List<Long> {
        return playerDao.getAllMultiplayerId()
    }

}