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
        binding.btnExitGame.setOnClickListener { viewGameOver() }

        val game = Game()
        val gameState = game.state
        startGame(gameState)

        //successChecker()
        //errorsChecker()
    }
    fun viewGameOver() {
        val intent = Intent(this, GameOverActivity::class.java)
        startActivity(intent)
    }

    private fun startGame(gameState: Boolean) {
        if (gameState) searchInfo()
    }

    /*private fun errorsChecker() {
        TODO("Not yet implemented")
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

    private fun successChecker(randomNumber: Int): Boolean {
        var correct = false
        when (randomNumber) {
            1 -> {
                binding.btnOption1.setOnClickListener { oneGreen(); correct = true
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({searchInfo()},1500) }
                binding.btnOption2.setOnClickListener { oneGreen(); correct = false }
                binding.btnOption3.setOnClickListener { oneGreen(); correct = false  }
            }
            2 -> {
                binding.btnOption1.setOnClickListener { twoGreen(); correct = false  }
                binding.btnOption2.setOnClickListener { twoGreen(); correct = true
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({searchInfo()},1500) }
                binding.btnOption3.setOnClickListener { twoGreen(); correct = false  }
            }
            else -> {
                binding.btnOption1.setOnClickListener { threeGreen(); correct = false  }
                binding.btnOption2.setOnClickListener { threeGreen(); correct = false  }
                binding.btnOption3.setOnClickListener { threeGreen(); correct = true
                    Handler(Looper.getMainLooper()).postDelayed({colorResetter(); clearPrices()},1000)
                    Handler(Looper.getMainLooper()).postDelayed({searchInfo()},1500) }
            }
        }
        return correct
    }

    private fun searchInfo() {
        fun searchCategories() {
            API().getCategories(object : Callback<List<Category>> {
                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                    if (response.isSuccessful) {
                        val categories = response.body()!!
                        val category = categories[(categories.indices).random()].id

                        fun searchItemFromCategory(id: String) {
                            API().getArticlesFromCategory(id, object : Callback<Articles> {
                                override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                                    if (response.isSuccessful) {
                                        response.body()!!.apply {
                                            val itemsList: MutableList<Article> = mutableListOf()
                                            itemsList.addAll(this.results)
                                            val item = itemsList[(itemsList.indices).random()]
                                            binding.tvProductName.text = item.title

                                            val randomNumber1to3 = (1..3).random()
                                            when (randomNumber1to3) {
                                                1 -> binding.btnOption1.text = item.price.toString()
                                                2 -> binding.btnOption2.text = item.price.toString()
                                                3 -> binding.btnOption3.text = item.price.toString()
                                                else -> println("Out of bounds")
                                            }

                                            successChecker(randomNumber1to3)

                                            fun searchItem(id:String) {
                                                API().getArticle(id, object : Callback<Article> {
                                                    override fun onResponse(call: Call<Article>, response: Response<Article>) {
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
                                                    override fun onFailure(call: Call<Article>, t: Throwable) {
                                                        Log.e("Main", "Falló al obtener el artículo", t)
                                                    }
                                                })
                                            }
                                            searchItem(item.id)
                                        }
                                    } else {
                                        println("Falló con código ${response.code()}")
                                    }
                                }

                                override fun onFailure(call: Call<Articles>, t: Throwable) {
                                    Log.e("Main", "Falló al obtener los articulos de la categoría", t)
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