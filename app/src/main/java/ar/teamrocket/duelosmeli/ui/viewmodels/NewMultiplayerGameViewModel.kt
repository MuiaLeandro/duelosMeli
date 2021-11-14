package ar.teamrocket.duelosmeli.ui.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class NewMultiplayerGameViewModel (val repository: PlayersRepository) : ViewModel()  {

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





}