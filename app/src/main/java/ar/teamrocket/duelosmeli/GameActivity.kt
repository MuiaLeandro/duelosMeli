package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import kotlin.math.roundToInt

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

    private fun timer(game: Game,  randomNumber: Int){
        var actualGame = game
        class Timer(millisInFuture: Long, countDownInterval: Long) :
            CountDownTimer(millisInFuture, countDownInterval) {
            override fun onFinish() {
                    when(randomNumber) {
                        1 -> oneGreen()
                        2 -> twoGreen()
                        else -> threeGreen()
                    }
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500)
            }
            override fun onTick(millisUntilFinished: Long) {
                //se muestra el conteo en textview
                binding.tvTime.setText((millisUntilFinished / 1000).toString() + "")
            }
        }
        val timer = Timer(11000, 1000)
        timer.start()
        when (randomNumber) {
            1 -> {
                binding.btnOption1.setOnClickListener {timer.cancel(); oneGreen()
                    pointsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption2.setOnClickListener {timer.cancel(); oneGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption3.setOnClickListener {timer.cancel(); oneGreen()
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
            }
            2 -> {
                binding.btnOption1.setOnClickListener {timer.cancel(); twoGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption2.setOnClickListener {timer.cancel(); twoGreen()
                    pointsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({continuePlayChecker(actualGame)},1500) }
                binding.btnOption3.setOnClickListener {timer.cancel(); twoGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
            }
            else -> {
                binding.btnOption1.setOnClickListener {timer.cancel(); threeGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption2.setOnClickListener {timer.cancel(); threeGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption3.setOnClickListener {timer.cancel(); threeGreen()
                    pointsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({continuePlayChecker(actualGame)},1500) }
            }
        }
    }


    private fun viewGameOver(game: Game) {
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("Points",game.points)
        startActivity(intent)
    }

    private fun playGame(game: Game): Game {
        var actualGame = game
        if (actualGame.state) actualGame = searchInfo(game)
        return actualGame
    }

    /*private fun finishedGame(game: Game) {
        TODO()
    }*/

    private fun clearPrices(){
        binding.btnOption1.text = ""
        binding.btnOption2.text = ""
        binding.btnOption3.text = ""
    }
    private fun colorResetter(){
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.purple_500, null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.purple_500, null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.purple_500, null))
    }

    private fun pointsCounter(game: Game){
        game.points++
    }

    private fun errorsCounter(game: Game) {
        game.errors++
    }

    private fun continuePlayChecker(game: Game): Game {
        if (game.state && game.errors < 3) searchInfo(game)
        if (game.errors == 3) {
            game.state = false
            Handler(Looper.getMainLooper()).postDelayed({ viewGameOver(game) }, 2000)
        }
        return game
    }

    private fun successChecker(randomNumber: Int, game: Game): Game {
        timer(game, randomNumber)
        return game
    }

    private fun searchInfo(game: Game): Game {
        var game = game
        if(game.state) {
            fun searchCategories() {
                API().getCategories(object : Callback<List<Category>> {
                    override fun onResponse(
                        call: Call<List<Category>>,
                        response: Response<List<Category>>
                    ) {
                        if (response.isSuccessful) {
                            val categories = response.body()!!
                            val category = categories[(categories.indices).random()].id

                            fun searchItemFromCategory(id: String) {
                                API().getArticlesFromCategory(id, object : Callback<Articles> {
                                    override fun onResponse(
                                        call: Call<Articles>,
                                        response: Response<Articles>
                                    ) {
                                        if (response.isSuccessful) {
                                            response.body()!!.apply {
                                                val itemsList: MutableList<Article> =
                                                    mutableListOf()
                                                itemsList.addAll(this.results)
                                                val item = itemsList[(itemsList.indices).random()]
                                                binding.tvProductName.text = item.title

                                                val randomNumber1to3 = (1..3).random()
                                                when (randomNumber1to3) {
                                                    1 -> binding.btnOption1.text =
                                                        item.price.toString()
                                                    2 -> binding.btnOption2.text =
                                                        item.price.toString()
                                                    3 -> binding.btnOption3.text =
                                                        item.price.toString()
                                                    else -> println("Out of bounds")
                                                }

                                                fun searchItem(id: String) {
                                                    API().getArticle(
                                                        id,
                                                        object : Callback<Article> {
                                                            override fun onResponse(
                                                                call: Call<Article>,
                                                                response: Response<Article>
                                                            ) {
                                                                if (response.isSuccessful) {
                                                                    response.body()!!.apply {
                                                                        Picasso.get()
                                                                            .load(this.pictures[0].secureUrl)
                                                                            .into(binding.ivProductPicture)
                                                                    }
                                                                } else {
                                                                    println("Falló con código ${response.code()}")
                                                                }
                                                            }

                                                            override fun onFailure(
                                                                call: Call<Article>,
                                                                t: Throwable
                                                            ) {
                                                                Log.e(
                                                                    "Main",
                                                                    "Falló al obtener el artículo",
                                                                    t
                                                                )
                                                            }
                                                        })
                                                }
                                                searchItem(item.id)
                                                randomPrices(item, randomNumber1to3)
                                                game = successChecker(randomNumber1to3, game)

                                                //continuePlayChecker(game)
                                            }
                                        } else {
                                            println("Falló con código ${response.code()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<Articles>, t: Throwable) {
                                        Log.e(
                                            "Main",
                                            "Falló al obtener los articulos de la categoría",
                                            t
                                        )
                                    }

                                })
                            }

                            searchItemFromCategory(category)

                        } else {
                            println("Falló con código ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                        Log.e("Main", "Falló al obtener las categorias", t)
                    }

                })
            }
            searchCategories()
        }
        //println(game.points)
        return game
    }

    private fun oneGreen() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red, null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red, null))
    }
    private fun twoGreen() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red, null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red, null))
    }
    private fun threeGreen() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red, null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red, null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null))
    }
    private fun randomPrices (item: Article, randomNumber: Int){

        val random1 = randomAddOrSubtract(item, randomNumber)
        var random2 = randomAddOrSubtract(item, randomNumber)
        if (random1.equals(random2)){
            random2 = randomAddOrSubtract(item, randomNumber)
        }
        randomOptions(randomNumber,
            random1, random2)
    }
    private fun randomOptions (correctOption: Int,
                               random1: Double, random2: Double){
        when (correctOption) {
            1 -> {binding.btnOption2.text = random1
                .toString()
                binding.btnOption3.text = random2
                    .toString()}
            2 -> {binding.btnOption1.text = random1
                .toString()
                binding.btnOption3.text = random2
                    .toString()}
            3 -> {binding.btnOption1.text = random1
                .toString()
                binding.btnOption2.text = random2
                    .toString()}
            else -> println("Out of bounds")
        }
    }
    private fun randomAddOrSubtract (item: Article, randomNumber: Int): Double{
        val realPrice = item.price
        val randomPrice = (1..8).random()
        var optionPrice = 0.0

        when (randomPrice){
            1 -> optionPrice = realPrice.times(1.10).roundToInt().toDouble()
            2 -> optionPrice = realPrice.times(1.15).roundToInt().toDouble()
            3 -> optionPrice = realPrice.times(1.20).roundToInt().toDouble()
            4 -> optionPrice = realPrice.times(1.25).roundToInt().toDouble()
            5 -> optionPrice = realPrice.times(0.90).roundToInt().toDouble()
            6 -> optionPrice = realPrice.times(0.85).roundToInt().toDouble()
            7 -> optionPrice = realPrice.times(0.80).roundToInt().toDouble()
            8 -> optionPrice = realPrice.times(0.85).roundToInt().toDouble()
            else -> println("Out of bounds")
        }
        return optionPrice
    }
}