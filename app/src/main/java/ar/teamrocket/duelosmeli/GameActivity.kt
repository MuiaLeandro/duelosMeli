package ar.teamrocket.duelosmeli

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import ar.teamrocket.duelosmeli.databinding.ActivityGameBinding
import ar.teamrocket.duelosmeli.model.Article
import ar.teamrocket.duelosmeli.model.Articles
import ar.teamrocket.duelosmeli.model.Category
import ar.teamrocket.duelosmeli.service.API
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import kotlin.math.roundToInt
import java.text.NumberFormat
import java.util.*


class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val game = Game()

        binding.btnExitGame.setOnClickListener { viewGameOver(game) }

        playGame(game)
    }

    private fun viewGameOver(game: Game) {
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("Points", game.points)
        startActivity(intent)
    }

    private fun playGame(game: Game): Game {
        var actualGame = game
        if (actualGame.state) actualGame = searchInfo(game)
        return actualGame
    }

    private fun searchInfo(game: Game): Game {
        if (game.state) searchCategories(game)
        return game
    }

    private fun searchCategories(game: Game) {
        API().getCategories(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val categories = response.body()!!
                    val categoryId = categories[(categories.indices).random()].id
                    searchItemFromCategory(categoryId, game)
                } else {
                    println("Falló con código ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.e("Main", "Falló al obtener las categorias", t)
            }
        })
    }

    fun searchItemFromCategory(id: String, currentGame: Game) {
        var actualGame = currentGame
        API().getArticlesFromCategory(id, object : Callback<Articles> {
            override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                if (response.isSuccessful) {
                    response.body()!!.apply {
                        val itemsList: MutableList<Article> = mutableListOf()
                        itemsList.addAll(this.results)
                        val item = itemsList[(itemsList.indices).random()]
                        binding.tvProductName.text = item.title

                        val price = numberRounder(item.price)

                        val randomNumber1to3 = (1..3).random()
                        when (randomNumber1to3) {
                            1 -> binding.btnOption1.text = price
                            2 -> binding.btnOption2.text = price
                            3 -> binding.btnOption3.text = price
                            else -> println("Out of bounds")
                        }
                        searchItem(item.id)
                        randomOptionsCalculator(item, randomNumber1to3)
                        actualGame = successChecker(randomNumber1to3, actualGame)
                    }
                } else {
                    println("Falló con código ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Articles>, t: Throwable) {
                Log.e("Main","Falló al obtener los articulos de la categoría", t)
            }
        })
    }

    private fun successChecker(correctOption: Int, game: Game): Game {
        timer(game, correctOption)
        return game
    }

    fun searchItem(id: String) {
        API().getArticle(id, object : Callback<Article> {
            override fun onResponse(call: Call<Article>,response: Response<Article>) {
                if (response.isSuccessful) {response.body()!!.apply {
                    Picasso.get()
                        .load(this.pictures[0].secureUrl)
                        .into(binding.ivProductPicture)}
                } else {
                    println("Falló con código ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Article>, t: Throwable) {
                Log.e("Main","Falló al obtener el artículo", t)
            }
        })
    }

    private fun timer(game: Game, correctOption: Int) {
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

        val timer = Timer(11000, 1000)
        timer.start()
        when (correctOption) {
            1 -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); oneCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); oneCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); oneCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
            }
            2 -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); twoCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); twoCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); twoCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
            }
            else -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); threeCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); threeCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); threeCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
            }
        }
    }

    private fun timerFunctions(game: Game){
        var actualGame = game
        binding.btnOption1.isClickable = false; binding.btnOption2.isClickable = false; binding.btnOption3.isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({ colorResetter(); clearPrices() },1000)
        Handler(Looper.getMainLooper()).postDelayed({ actualGame = continuePlayChecker(actualGame) },1500)
    }

    private fun continuePlayChecker(game: Game): Game {
        if (game.state && game.errors < 3) searchInfo(game)
        if (game.errors == 3) {
            game.state = false
            Handler(Looper.getMainLooper()).postDelayed({ viewGameOver(game) }, 2000)
        }
        return game
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

    private fun numberRounder(numberDouble: Double): String {
        val numberFormatter: NumberFormat = NumberFormat.getNumberInstance(Locale.GERMAN)
        numberFormatter.roundingMode = RoundingMode.FLOOR
        return numberFormatter.format(numberDouble.toInt())
    }

    private fun randomOptionsCalculator(item: Article, correctOptionPosition: Int) {
        val randomPrice1 = randomPriceCalculator(item)
        var randomPrice2 = randomPriceCalculator(item)

        while (randomPrice1.equals(randomPrice2)) {
            randomPrice2 = randomPriceCalculator(item)
        }
        randomOptionsPosition(correctOptionPosition, randomPrice1, randomPrice2)
    }

    private fun randomOptionsPosition(correctOptionPosition: Int,
                                      randomCalculatedPrice1: Double,
                                      randomCalculatedPrice2: Double) {
        when (correctOptionPosition) {
            1 -> {
                binding.btnOption2.text = numberRounder(randomCalculatedPrice1)
                binding.btnOption3.text = numberRounder(randomCalculatedPrice2)
            }
            2 -> {
                binding.btnOption1.text = numberRounder(randomCalculatedPrice1)
                binding.btnOption3.text = numberRounder(randomCalculatedPrice2)
            }
            3 -> {
                binding.btnOption1.text = numberRounder(randomCalculatedPrice1)
                binding.btnOption2.text = numberRounder(randomCalculatedPrice2)
            }
            else -> println("Out of bounds")
        }
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

    private fun clearPrices() {
        binding.btnOption1.text = ""
        binding.btnOption2.text = ""
        binding.btnOption3.text = ""
    }

    private fun colorResetter() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.purple_500,null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.purple_500,null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.purple_500,null))
    }
}