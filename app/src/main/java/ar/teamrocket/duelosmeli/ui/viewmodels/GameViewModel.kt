package ar.teamrocket.duelosmeli.ui.viewmodels

import android.content.Context
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

    fun findCategories(
        context: Context, viewRoot: View) {
        meliRepositoryImpl.searchCategories({
            val categories = it
            val categoryId = categories[(categories.indices).random()].id
            findItemFromCategory(categoryId, context, viewRoot)

            //Obtengo en esta instancia un numero random para tenerlo antes de bindear los precios
            randomNumber1to3Mutable.value = (1..3).random()
        }, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }, {
            Snackbar.make(viewRoot, R.string.no_internet, Snackbar.LENGTH_LONG).show()
            Log.e("Main", "Falló al obtener las categorias", it)
        })
    }

    fun findItemFromCategory(categoryId: String, context: Context, viewRoot: View) {
        meliRepositoryImpl.searchItemFromCategory(categoryId, {
            apply {
                val itemsList: MutableList<Article> = mutableListOf()
                itemsList.addAll(it.results)
                val item = itemsList[(itemsList.indices).random()]
                itemNameMutable.value = item.title

                itemPriceString.value = numberRounder(item.price)

                searchItem(item.id, context, viewRoot)
                randomOptionsCalculator(item)
            }
        }, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }, {
            Snackbar.make(viewRoot, R.string.no_internet, Snackbar.LENGTH_LONG).show()
            Log.e("Main", "Falló al obtener los articulos de la categoría", it)
        })
    }

    fun numberRounder(numberDouble: Double): String {
        val numberFormatter: NumberFormat = NumberFormat.getNumberInstance(Locale.GERMAN)
        numberFormatter.roundingMode = RoundingMode.FLOOR
        return numberFormatter.format(numberDouble.toInt())
    }

    fun searchItem(id: String, context: Context, viewRoot: View) {
        meliRepositoryImpl.searchItem(id, {
            apply {
                picture.value = it.pictures[0].secureUrl
            }
        }, {
            Toast.makeText(context, it,Toast.LENGTH_LONG).show()
        }, {
            Snackbar.make(viewRoot, R.string.no_internet, Snackbar.LENGTH_LONG).show()
            Log.e("Main", "Falló al obtener el artículo", it)
        })
    }

    fun randomOptionsCalculator(item: Article) {
        val randomPrice1 = randomPriceCalculator(item)
        var randomPrice2 = randomPriceCalculator(item)

        if (randomPrice1.equals(randomPrice2)){
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

    private fun randomOptionsPosition(randomCalculatedPrice1: Double,
                                      randomCalculatedPrice2: Double) {
        fakePrice1.value = numberRounder(randomCalculatedPrice1)
        fakePrice2.value = numberRounder(randomCalculatedPrice2)
    }

    /*fun successChecker(correctOption: Int, game: Game): Game {
        timer(game, correctOption)
        return game
    }

    fun timer(game: Game, correctOption: Int) {
        var actualGame = game

        class Timer(millisInFuture: Long, countDownInterval: Long) :
            CountDownTimer(millisInFuture, countDownInterval) {
            override fun onFinish() {
                when (correctOption) {
                    1 -> oneCorrect()
                    2 -> twoCorrect()
                    else -> threeCorrect()
                }
                game.errorsCounter(actualGame); timerFunctions(actualGame)
            }
            override fun onTick(millisUntilFinished: Long) {
                //se muestra el conteo en textview
                binding.tvTime.text = (millisUntilFinished / 1000).toString()
            }
        }

        val timer = Timer(21000, 1000)
        timer.start()
        when (correctOption) {
            1 -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,true); oneCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); oneCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); oneCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
            }
            2 -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); twoCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,true); twoCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); twoCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
            }
            else -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); threeCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); threeCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,true); threeCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
            }
        }
    }

    private fun oneCorrect() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green,null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
    }

    private fun twoCorrect() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
    }

    private fun threeCorrect() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green,null))
    }

    private fun timerFunctions(game: Game){
        var actualGame = game
        binding.btnOption1.isClickable = false; binding.btnOption2.isClickable = false; binding.btnOption3.isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({ colorResetter() },1000)
        Handler(Looper.getMainLooper()).postDelayed({ actualGame = continuePlayChecker(actualGame) },1500)

        gameFunctions.mistakeCounterUpdater(game, binding.ivLifeThree, binding.ivLifeTwo, binding.ivLifeOne)
    }

    private fun continuePlayChecker(game: Game): Game {

        if (game.state && game.errors < 3) searchInfo(game)
        if (game.errors == 3) {
            game.state = false

            val database = Room.databaseBuilder(
                applicationContext,
                DuelosMeliDb::class.java,
                "duelosmeli-db"
            ).allowMainThreadQueries().build()
            val playerDao = database.playerDao()

            //actualizar el jugador:
            var player = playerDao.getById(game.playerId)
            if (player.isNotEmpty() && game.points > player[0].score ) {
                player[0].score = game.points
                playerDao.updatePlayer(player[0])
            }
            //ir a GameOver:
            Handler(Looper.getMainLooper()).postDelayed({ viewGameOver(game) }, 2000)
        }
        return game
    }

    private fun colorResetter(){
        binding.btnOption1.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
        binding.btnOption2.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
        binding.btnOption3.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
    }

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }*/
}