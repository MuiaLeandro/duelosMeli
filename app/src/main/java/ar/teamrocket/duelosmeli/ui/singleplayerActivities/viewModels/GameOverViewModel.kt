package ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.teamrocket.duelosmeli.data.database.Player
import ar.teamrocket.duelosmeli.data.repository.PlayerRepository
import kotlinx.coroutines.launch

class GameOverViewModel(private val repo: PlayerRepository) : ViewModel() {
    val topTenPlayers = MutableLiveData<List<Player>>()
    val currentPlayer = MutableLiveData<Player>()

    fun setupTopTenPlayers() {
        viewModelScope.launch {
            topTenPlayers.value = repo.getTopTenPlayersOrderByScore()
        }
    }

    fun setupCurrentPlayer(id: Long) {
        viewModelScope.launch {
            currentPlayer.value = repo.getPlayerById(id).firstOrNull()
        }
    }
}