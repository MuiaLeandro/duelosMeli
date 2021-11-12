package ar.teamrocket.duelosmeli.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MultiplayerGamePartialResultActivityViewModel(application: Application) : AndroidViewModel(application) {
    private  var repository: PlayersRepository = PlayersRepository(application)
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
        if (indexCurrentPlayer != null && playersOrderByScore.value != null) {
            currentPlayer.value = playersOrderByScore.value!![indexCurrentPlayer]
        }
    }

    fun setAddPoint(addPointP: Boolean) {
        addPoint.value = addPointP
    }

}