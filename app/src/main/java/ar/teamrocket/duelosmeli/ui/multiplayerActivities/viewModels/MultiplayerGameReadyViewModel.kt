package ar.teamrocket.duelosmeli.ui.multiplayerActivities.viewModels

import androidx.lifecycle.*
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import kotlinx.coroutines.launch

class MultiplayerGameReadyViewModel (val repository: PlayersRepository): ViewModel() {
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