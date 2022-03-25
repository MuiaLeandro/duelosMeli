package ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class GameViewModel (val meliRepositoryImpl : MeliRepository) : ViewModel() {

    private val systemLanguage: String = Locale.getDefault().language
    private lateinit var categories: List<Category>
    private lateinit var categoryId: String
    lateinit var items: Articles
    val itemNameMutable = MutableLiveData<String>()
    val picture = MutableLiveData<String>()
    val itemPriceString = MutableLiveData<String>()
    val randomNumber1to3Mutable = MutableLiveData<Int>()
    val fakePrice1 = MutableLiveData<String>()
    val fakePrice2 = MutableLiveData<String>()
    val starGame = MutableLiveData(true)
    val categoriesException = MutableLiveData<Throwable>()
    val itemFromCategoryException = MutableLiveData<Throwable>()
    val itemException = MutableLiveData<Throwable>()


    fun findCategories() {
        viewModelScope.launch {
            starGame.value = false
            try {
                categories = when(systemLanguage) {
                    "pt" -> meliRepositoryImpl.searchCategoriesBR()
                    else -> meliRepositoryImpl.searchCategories()
                }
                categoryId = categories[(categories.indices).random()].id
                findItemFromCategory(categoryId)

                //Obtengo en esta instancia un numero random para tenerlo antes de bindear los precios
                randomNumber1to3Mutable.value = (1..3).random()
            } catch (e: Exception){
                categoriesException.value = e
            }
        }
    }

    fun findItemFromCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                items = when(systemLanguage) {
                    "pt" -> meliRepositoryImpl.searchItemFromCategoryBR(categoryId)
                    else -> meliRepositoryImpl.searchItemFromCategory(categoryId)
                }
                val itemsList: MutableList<Article> = mutableListOf()
                itemsList.addAll(items.results)

                val item = itemsList[(itemsList.indices).random()]

                itemNameMutable.value = item.title

                itemPriceString.value = numberRounder(item.price)

                searchItem(item.id)
                randomOptionsCalculator(item)
            } catch (e: Exception) {
                itemFromCategoryException.value = e
            }
        }
    }

    fun numberRounder(numberDouble: Double): String {
        val numberFormatter: NumberFormat = NumberFormat.getNumberInstance(Locale.GERMAN)
        numberFormatter.roundingMode = RoundingMode.FLOOR
        return numberFormatter.format(numberDouble.toInt())
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

    fun randomOptionsCalculator(item: Article) {
        val randomPrice1 = randomPriceCalculator(item)
        var randomPrice2 = randomPriceCalculator(item)

        if (randomPrice1.equals(randomPrice2)) {
            while (randomPrice1.equals(randomPrice2)) {
                randomPrice2 = randomPriceCalculator(item)
                if (randomPrice1 != randomPrice2) randomOptionsPosition(randomPrice1, randomPrice2)
            }
        } else randomOptionsPosition(randomPrice1, randomPrice2)

        //randomOptionsPosition(randomPrice1, randomPrice2)
    }

    private fun randomPriceCalculator(item: Article): Double {
        val realPrice = item.price
        val randomNumber = (1..8).random()
        var fakePrice = 0.0

        when (randomNumber) {
            1 -> fakePrice = realPrice.times(1.10).roundToInt().toDouble()
            2 -> fakePrice = realPrice.times(1.15).roundToInt().toDouble()
            3 -> fakePrice = realPrice.times(1.20).roundToInt().toDouble()
            4 -> fakePrice = realPrice.times(1.25).roundToInt().toDouble()
            5 -> fakePrice = realPrice.times(0.90).roundToInt().toDouble()
            6 -> fakePrice = realPrice.times(0.85).roundToInt().toDouble()
            7 -> fakePrice = realPrice.times(0.80).roundToInt().toDouble()
            8 -> fakePrice = realPrice.times(0.75).roundToInt().toDouble()
            else -> println("Out of bounds")
        }
        return fakePrice
    }

    private fun randomOptionsPosition(
        randomCalculatedPrice1: Double,
        randomCalculatedPrice2: Double
    ) {
        fakePrice1.value = numberRounder(randomCalculatedPrice1)
        fakePrice2.value = numberRounder(randomCalculatedPrice2)
    }
}