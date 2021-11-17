package ar.teamrocket.duelosmeli.ui.viewmodels

import androidx.lifecycle.*
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import ar.teamrocket.duelosmeli.domain.multiplayerActivities.GameMultiplayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MultiplayerGamePartialResultActivityViewModel(val repository: PlayersRepository) : ViewModel() {
    var team = MutableLiveData<List<Multiplayer>>()
    var playersOrderByScore = MutableLiveData<List<Multiplayer>>()
    var game = MutableLiveData<GameMultiplayer>()
    var currentPlayer = MutableLiveData<Multiplayer>()
    var addPoint = MutableLiveData<Boolean>()

    fun setAllMultiplayerOrderByScore() {
        viewModelScope.launch {
            playersOrderByScore.value = repository.getAllMultiplayersOrderByScore()
        }
    }
    fun getAllMultiplayerOrderByScore(): LiveData<List<Multiplayer>> {
        return playersOrderByScore
    }

    fun setListMultiplayers() {
        viewModelScope.launch {
            team.value = repository.getAllMultiplayers()
        }
    }

    fun getAllMultiplayer(): LiveData<List<Multiplayer>>{
        return team
    }

    fun addPointToThePlayer(currentPlayer: Multiplayer) {
        currentPlayer.score++
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMultiplayer(currentPlayer)
            setListMultiplayers()
            setAllMultiplayerOrderByScore()
            setCurrentPlayer()
        }
    }

    fun setGame(gameP: GameMultiplayer) {
        game.value = gameP
    }

    fun setCurrentPlayer() {
        val indexCurrentPlayer = game.value?.currentPlayer
        if (indexCurrentPlayer != null && team.value != null) {
            currentPlayer.postValue(team.value!![indexCurrentPlayer])
        }
    }

    fun setAddPoint(addPointP: Boolean) {
        addPoint.value = addPointP
    }

}