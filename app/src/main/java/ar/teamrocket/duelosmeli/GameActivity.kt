package ar.teamrocket.duelosmeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        searchInfo()

    }

    private fun searchInfo() {
        fun searchCategories() {
            API().getCategories(object : Callback<List<Category>> {
                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                    if (response.isSuccessful) {
                        val categories = response.body()!!
                        val category = categories.get((categories.indices).random()).id
                        //println("La categoría es: $category")

                        fun searchItemFromCategory(id: String) {
                            API().getArticlesFromCategory(id, object : Callback<Articles> {
                                override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                                    if (response.isSuccessful) {
                                        response.body()!!.apply {
                                            var itemsList: MutableList<Article> = mutableListOf()
                                            itemsList.addAll(this.results)
                                            val item = itemsList.get((itemsList.indices).random())
                                            binding.tvProductName.text = item.title

                                            val randomNumber1to3 = (1..3).random()
                                            when (randomNumber1to3) {
                                                1 -> binding.btnOption1.text = item.price.toString()
                                                2 -> binding.btnOption2.text = item.price.toString()
                                                3 -> binding.btnOption3.text = item.price.toString()
                                                else -> println("Out of bounds")
                                            }


                                            val id = item.id

                                            // Esta es la funcion de buscar un item por su ID
                                            // La uso para obtener una picture, para que vean la diferencia
                                            // de calidad de la imagen comparado con el thumbnail
                                            fun searchItem(id:String) {
                                                API().getArticle(id, object : Callback<Article> {
                                                    override fun onResponse(call: Call<Article>, response: Response<Article>) {
                                                        if (response.isSuccessful) {
                                                            response.body()!!.apply {
                                                                //println("URL de la imagen del producto: ${this.pictures[0].secureUrl}")
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
                                            searchItem(id)
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
}