package ar.teamrocket.duelosmeli.ui.multiplayerActivities.viewModels

import androidx.lifecycle.*
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import ar.teamrocket.duelosmeli.domain.GameMultiplayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MultiplayerGameViewModel(val meliRepositoryImpl: MeliRepository, var repository: PlayersRepository) : ViewModel() {

    val itemNameMutable = MutableLiveData<String>()
    val picture = MutableLiveData<String>()
    val starGame = MutableLiveData(true)
    val categoriesException = MutableLiveData<Throwable>()
    val itemFromCategoryException = MutableLiveData<Throwable>()
    val itemException = MutableLiveData<Throwable>()
    var game = MutableLiveData<GameMultiplayer>()
    var currentPlayer = MutableLiveData<Multiplayer>()
    var playersOrderByScore = MutableLiveData<List<Multiplayer>>()
    var team = MutableLiveData<List<Multiplayer>>()


    fun findCategories() {
        viewModelScope.launch {
            starGame.value = false
            try {
                val categories = meliRepositoryImpl.searchCategories()
                val categoryId = categories[(categories.indices).random()].id
                findItemFromCategory(categoryId)

            } catch (e: Exception){
                categoriesException.value = e
            }
        }
    }

    fun findItemFromCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                val items = meliRepositoryImpl.searchItemFromCategory(categoryId)
                val itemsList: MutableList<Article> = mutableListOf()
                itemsList.addAll(items.results)

                val item = itemsList[(itemsList.indices).random()]

                itemNameMutable.value = item.title

                searchItem(item.id)
            } catch (e: Exception) {
                itemFromCategoryException.value = e
            }
        }
    }

    fun searchItem(id: String) {
        viewModelScope.launch {
            try {
                val article = meliRepositoryImpl.searchItem(id)
                picture.value = article.pictures[0].secureUrl
            } catch (e: Exception){
                itemException.value = e
            }
        }
    }

    fun setCurrentPlayer() {
        val indexCurrentPlayer = game.value?.currentPlayer
        val team = team.value
        if (indexCurrentPlayer != null && team != null) {
            currentPlayer.postValue(team[indexCurrentPlayer])
            //currentPlayer.value = team[indexCurrentPlayer]
        }
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

    fun setGame(game: GameMultiplayer) {
        this.game.value = game
    }


}