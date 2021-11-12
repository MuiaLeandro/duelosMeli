package ar.teamrocket.duelosmeli.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import kotlinx.coroutines.launch

class MultiplayerGameReadyViewModel(application: Application) : AndroidViewModel(application) {
    private  var repository: PlayersRepository = PlayersRepository(application)        //No es una buena práctica pasar el contexto de su actividad al modelo de vista de la actividad, ya que es una pérdida de memoria.
    var team = MutableLiveData<List<Multiplayer>>()
    var allPlayersId = MutableLiveData<List<Long>>()

    fun setListMultiplayers() {
        viewModelScope.launch {
            team.value = repository.getAllMultiplayers()
        }
    }

    fun setListMultiplayersId() {
        viewModelScope.launch {
            allPlayersId.value = repository.getAllMultiplayersId()
        }
    }

    fun  getListMultiplayersLiveData():LiveData<List<Multiplayer>>{
        return team
    }

    fun  getListMultiplayersIdLiveData():LiveData<List<Long>>{
        return allPlayersId
    }

}