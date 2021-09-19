package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val game = Game()

        binding.btnExitGame.setOnClickListener { viewGameOver(game) }


        //val gameState = game.state
        /*while (game.state == true) {
            var actualGame = searchInfo(game)
            game.state = actualGame.state
        }*/
        //var actualGame = playGame(game)
        playGame(game)
        //finishedGame(actualGame)
        //searchInfo(game)


        //finishedGame = lo que retorne el playGame (meter el while dentro de playGame y hacer que retorne un Game)
        //despues puedo obtener finishedGame.status, points, etc....

        //successChecker()
        //errorsCounter()
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
        //var correct = false
        var actualGame = game
        when (randomNumber) {
            1 -> {
                binding.btnOption1.setOnClickListener { oneGreen()
                    pointsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption2.setOnClickListener { oneGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption3.setOnClickListener { oneGreen()
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
            }
            2 -> {
                binding.btnOption1.setOnClickListener { twoGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption2.setOnClickListener { twoGreen()
                    pointsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({continuePlayChecker(actualGame)},1500) }
                binding.btnOption3.setOnClickListener { twoGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
            }
            else -> {
                binding.btnOption1.setOnClickListener { threeGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption2.setOnClickListener { threeGreen()
                    errorsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({actualGame = continuePlayChecker(actualGame)},1500) }
                binding.btnOption3.setOnClickListener { threeGreen()
                    pointsCounter(actualGame)
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({continuePlayChecker(actualGame)},1500) }
            }
        }
        return actualGame
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
}