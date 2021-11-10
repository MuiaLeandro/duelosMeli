package ar.teamrocket.duelosmeli.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewMultiplayerGameViewModel(application: Application) : AndroidViewModel(application)  {
     private  var repository: PlayersRepository = PlayersRepository(application)        //No es una buena práctica pasar el contexto de su actividad al modelo de vista de la actividad, ya que es una pérdida de memoria.
     var team = MutableLiveData<List<Multiplayer>>()                  //Por lo tanto, para obtener el contexto en su ViewModel, la clase ViewModel debería extender la clase Android View Model

    fun setListMultiplayers() {
        viewModelScope.launch {
            team.value = repository.getAllMultiplayers()
        }
    }

    fun  getListMultiplayersLiveData():LiveData<List<Multiplayer>>{
        return team
    }

    fun insertMultiplayer(multiplayer: Multiplayer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMultiplayer(multiplayer)
            setListMultiplayers()
        }
    }

    fun deleteAllMultiplayer(team: List<Multiplayer>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMultiplayer(team)
            setListMultiplayers()
        }
    }

    fun deleteMultiplayer(multiplayer: Multiplayer) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMultiplayer(multiplayer)
        setListMultiplayers()
    }




//    private lateinit var database: DuelosMeliDb
//    private lateinit var playerDao: PlayerDao
//
//
//    private var team: MutableLiveData<List<Multiplayer>> = MutableLiveData()
//    init {
//        injectDependencies()
//        team = playerDao.getAllMultiplayer()
//
//    }
//    companion object {
//        private fun buildDatabase(context: Context): DuelosMeliDb {
//            return Room.databaseBuilder(
//                context,
//                DuelosMeliDb::class.java,
//                "duelosmeli-db"
//            ).allowMainThreadQueries().build()
//        }
//    }
//
//    /**
//     * Manual dependency injection
//     */
//    private fun injectDependencies() {
//        this.database = buildDatabase((getApplication()))   //No es una buena práctica pasar el contexto de su actividad al modelo de vista de la actividad, ya que es una pérdida de memoria.
//                                                            //Por lo tanto, para obtener el contexto en su ViewModel, la clase ViewModel debería extender la clase Android View Model
//        this.playerDao = this.database.playerDao()
//    }
//
//    fun getAllMultiplayersObservers(): MutableLiveData<List<Multiplayer>>{
//        return team
//    }



}