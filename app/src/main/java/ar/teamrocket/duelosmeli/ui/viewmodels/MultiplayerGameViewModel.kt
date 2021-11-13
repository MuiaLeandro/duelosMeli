package ar.teamrocket.duelosmeli.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import ar.teamrocket.duelosmeli.data.repository.impl.MeliRepositoryImpl
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class MultiplayerGameViewModel(application: Application) : AndroidViewModel(application) {
    private val meliRepositoryImpl: MeliRepository = MeliRepositoryImpl()
    private  var repository: PlayersRepository = PlayersRepository(application)


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

                //Obtengo en esta instancia un numero random para tenerlo antes de bindear los precios
                //randomNumber1to3Mutable.value = (1..3).random()
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
        if (indexCurrentPlayer != null && playersOrderByScore.value != null) {
            currentPlayer.value = playersOrderByScore.value!![indexCurrentPlayer]
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




}