package ar.teamrocket.duelosmeli.ui.viewmodels

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Category
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.repository.impl.MeliRepositoryImpl
import ar.teamrocket.duelosmeli.domain.Game
import ar.teamrocket.duelosmeli.domain.GameFunctions
import ar.teamrocket.duelosmeli.domain.impl.GameFunctionsImpl
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class GameViewModel : ViewModel() {
    private val meliRepositoryImpl: MeliRepository = MeliRepositoryImpl()

    //var randomPrice1 by Delegates.notNull<Double>()
    //var randomPrice2 by Delegates.notNull<Double>()
    private var gameFunctions: GameFunctions = GameFunctionsImpl()
    val itemNameMutable = MutableLiveData<String>()
    val picture = MutableLiveData<String>()
    val itemPriceString = MutableLiveData<String>()
    val randomNumber1to3Mutable = MutableLiveData<Int>()
    val fakePrice1 = MutableLiveData<String>()
    val fakePrice2 = MutableLiveData<String>()
    val starGame = MutableLiveData(true)
    val toastCategory = MutableLiveData<String>()
    val onFailureCategory = MutableLiveData<Throwable>()
    val toastItemFromCategory = MutableLiveData<String>()
    val onFailureItemFromCategory = MutableLiveData<Throwable>()
    val toastItem = MutableLiveData<String>()
    val onFailureItem = MutableLiveData<Throwable>()

    fun startSound(context: Context){
        val doorbellSound = MediaPlayer.create(context, R.raw.doorbell)
        doorbellSound.start()
    }

    fun findCategories() {
        viewModelScope.launch {
            starGame.value = false
            meliRepositoryImpl.searchCategories({
                val categories = it
                val categoryId = categories[(categories.indices).random()].id
                findItemFromCategory(categoryId)

                //Obtengo en esta instancia un numero random para tenerlo antes de bindear los precios
                randomNumber1to3Mutable.value = (1..3).random()
            }, {
                toastCategory.value = it.toString()
            }, {
                onFailureCategory.value = it
            })
        }
    }

    fun findItemFromCategory(categoryId: String) {
        viewModelScope.launch {
            meliRepositoryImpl.searchItemFromCategory(categoryId, {
                apply {
                    val itemsList: MutableList<Article> = mutableListOf()
                    itemsList.addAll(it.results)
                    val item = itemsList[(itemsList.indices).random()]
                    itemNameMutable.value = item.title

                    itemPriceString.value = numberRounder(item.price)

                    searchItem(item.id)
                    randomOptionsCalculator(item)
                }
            }, {
                toastItemFromCategory.value = it.toString()
            }, {
                onFailureItemFromCategory.value = it
            })
        }
    }

    fun numberRounder(numberDouble: Double): String {
        val numberFormatter: NumberFormat = NumberFormat.getNumberInstance(Locale.GERMAN)
        numberFormatter.roundingMode = RoundingMode.FLOOR
        return numberFormatter.format(numberDouble.toInt())
    }

    fun searchItem(id: String) {
        viewModelScope.launch {
            meliRepositoryImpl.searchItem(id, {
                apply {
                    picture.value = it.pictures[0].secureUrl
                }
            }, {
                toastItem.value = it.toString()
            }, {
                onFailureItem.value = it
            })
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