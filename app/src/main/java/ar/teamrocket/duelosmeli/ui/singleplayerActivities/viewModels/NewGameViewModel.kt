package ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.teamrocket.duelosmeli.data.database.Player
import ar.teamrocket.duelosmeli.data.repository.PlayerRepository
import kotlinx.coroutines.launch

class NewGameViewModel(private val repo: PlayerRepository) : ViewModel() {
    val allPlayersByName = MutableLiveData<List<Player>>()
    val newPlayerInserted = MutableLiveData<Player>()

    fun setupAllPlayers() {
        viewModelScope.launch {
            allPlayersByName.value = repo.getAllPlayerOrderByName()
        }
    }

    fun playerAlreadyExist(playerName: Editable?): Boolean {
        playerName.toString().replace(" ", "")
        var playerNameAlreadyExists = false
        allPlayersByName.value?.let { players ->
            for (player in players) {
                if (player.name == playerName.toString()) {
                    playerNameAlreadyExists = true
                    break
                }
            }
        }
        return playerNameAlreadyExists
    }

    fun playerNameIsBlank(playerName: Editable?): Boolean {
        return playerName.toString().replace(" ", "").isBlank()
    }

    fun insertNewPlayer(newPlayerName: Editable?) {
        val newPlayer = Player(newPlayerName.toString(), 0) // como el id es autogenerado se le pone 0 y room ya sabe que id poner
        viewModelScope.launch {
            repo.insertPlayer(newPlayer) //Guardo el nuevo jugador
            newPlayerInserted.value = repo.getPlayerByName(newPlayer.name)[0] // obtengo el nuevo jugador desde la DB.
        }
    }
}
