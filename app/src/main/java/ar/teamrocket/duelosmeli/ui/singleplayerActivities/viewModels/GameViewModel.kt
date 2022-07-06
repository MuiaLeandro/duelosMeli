package ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
import ar.teamrocket.duelosmeli.data.model.ItemDuel
import ar.teamrocket.duelosmeli.data.preferences.Prefs
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt


class GameViewModel (val meliRepositoryImpl : MeliRepository, private val prefs: Prefs) : ViewModel() {

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
    private var itemTitle: String = ""
    private var itemPrice: String = ""
    private var itemPicture: String = ""
    private var itemFakePrice1 = ""
    private var itemFakePrice2 = ""
    private var itemCorrectOption: Int = 0
    val itemDuel = MutableLiveData<ItemDuel>()


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
                itemCorrectOption = randomNumber1to3Mutable.value!!
            } catch (e: Exception){
                categoriesException.value = e
            }
        }
    }

    /*
    *
    * Si el lenguaje del dispositivo no esta en portugues y la configuracion del juego esta
    * habilitada para jugar con ubicacion y nunca antes se habia guardado el ID de la provincia
    * (state), entonces se hace una llamada a la API para que traiga los items de esa categoria sin
    * filtros de ubicacion y dentro del response busca el filtro de "STATE" para encontrar el ID de
    * la provincia y lo guarda en Preferences para ya tenerlo en futuras consultas. Luego vuelve a
    * hacer el llamado a la API para obtener la lista de items pero filtrada por STATE (Provincia).
    * --------------------------------------------------------------------------------------------
    * Si el ID de la provincia ya esta guardado en SharedPreferences hace la llamada a la API
    * directamente con el id de la categoria y el de la provincia y nada mas.
    * --------------------------------------------------------------------------------------------
    * Si el juego por ubicacion esta deshabilitado se hace la llamada a la API con el ID de la
    * categoria nada mas.
    *
    *
    * */

    fun findItemFromCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                if (systemLanguage == "pt") {
                    items = meliRepositoryImpl.searchItemFromCategoryBR(categoryId)
                } else {
                    if (prefs.getLocationEnabled()) {
                        if (prefs.getLocationStateId() == "") {
                            items = meliRepositoryImpl.searchItemFromCategory(categoryId)
                            val stateFilter =
                                items.available_filters.first { filter -> filter.id == "state" }
                            val state = stateFilter.values.firstOrNull { state ->
                                state.name.uppercase(Locale.getDefault()) == prefs.getLocationState()
                                    .uppercase(Locale.getDefault())
                            }
                            prefs.saveLocationStateId(state?.id ?: "")
                            items = meliRepositoryImpl.searchItemFromCategory(
                                categoryId,
                                prefs.getLocationStateId(),
                                prefs.getLocationCityId()
                            )
                        } else {
                            items = meliRepositoryImpl.searchItemFromCategory(
                                categoryId,
                                prefs.getLocationStateId(),
                                prefs.getLocationCityId()
                            )
                        }
                    } else {
                        items = meliRepositoryImpl.searchItemFromCategory(categoryId)
                    }
                }
                val itemsList: MutableList<Article> = mutableListOf()
                itemsList.addAll(items.results)

                val item = itemsList[(itemsList.indices).random()]
                itemNameMutable.value = item.title
                itemTitle = itemNameMutable.value!!

                itemPriceString.value = numberRounder(item.price)
                itemPrice = itemPriceString.value!!

                Log.d("ITEM_ID","ITEM: ${item.id} CATEGORY: $categoryId")
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
                itemPicture = picture.value!!

                val itemDuelObject = ItemDuel("item",
                    itemTitle,
                    itemPrice,
                    itemPicture,
                    itemFakePrice1,
                    itemFakePrice2,
                    itemCorrectOption
                        )
                itemDuel.value = itemDuelObject
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


        itemFakePrice1 = numberRounder(randomPrice1)
        itemFakePrice2 = numberRounder(randomPrice2)
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