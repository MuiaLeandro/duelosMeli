package ar.teamrocket.duelosmeli.ui.multiplayerActivities.viewModels

import androidx.lifecycle.*
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NewMultiplayerGameViewModel (val repository: PlayersRepository) : ViewModel()  {
    var team = MutableLiveData<List<Multiplayer>>()
    var allPlayersId = MutableLiveData<List<Long>>()

    fun setListMultiplayers() {
        viewModelScope.launch {
            team.value = repository.getAllMultiplayers()
            var players = team.value
            for (player in players!!){
                player.score = 0
            }
            repository.updateMultiplayers(players)
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

    fun inicializeScores() {
        if (team.value?.isNotEmpty() == true) {
            var players = team.value

            for (player in players!!){
                player.score = 0
            }
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateMultiplayers(players)
            }
        }
    }


}