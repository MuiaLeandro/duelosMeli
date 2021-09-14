package ar.teamrocket.duelosmeli

import ar.teamrocket.duelosmeli.model.Article
import ar.teamrocket.duelosmeli.model.Articles
import ar.teamrocket.duelosmeli.model.Categories
import ar.teamrocket.duelosmeli.model.Category
import ar.teamrocket.duelosmeli.service.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

fun main(args: Array<String>) {
    // Your code here!

    /*fun test(){
        var asd: MutableList<Int> = mutableListOf()

        asd.add(1)
        print(asd[0])
    }

    test()*/

    //Hardcodeo una lista de categorias
    var categoria: MutableList<String> = mutableListOf()
    categoria.add("MLA5725")
    categoria.add("MLA1512")
    categoria.add("MLA1403")
    categoria.add("MLA1071")

    //Funcion para obtener una categoria random desde una lista
    fun randomCategory(categoriesList: List<String>): String {
        val randomNumber = (categoriesList.indices).random()
        return categoriesList[randomNumber]
    }
    val randomCategory = randomCategory(categoria)
    println(randomCategory)

    searchItem()
    searchCategories()
    //recibeIds(categoriesList)
    searchItemFromCategory(randomCategory)
}

/*private var categoriesList = mutableListOf<Category>()

fun recibeIds(categories: List<Category>?) {
    categoriesList.clear()
    if (categories != null){
        categoriesList.addAll(categories)
    }
}

var categoria: MutableList<Int> = mutableListOf()*/

// Fun para buscar items de una categoria
fun searchItemFromCategory(id: String) {
    API().getArticlesFromCategory(id, object : Callback<Articles> {
        override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
            if (response.isSuccessful) {
                response.body()!!.apply {
                    println(this.results[0].id)
                }
            } else {
                println("Falló con código ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Articles>, t: Throwable) {
            println("Falló al obtener el artículo :(")
        }

    })
}

// Fun para buscar las categorias
fun searchCategories() {
    API().getCategories(object : Callback<Categories> {
        override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
            if (response.isSuccessful) {
                response.body()!!.apply {
                    println("Successful")
                }
            } else {
                println("Falló con código ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Categories>, t: Throwable) {
            println("Falló al obtener las categorias :(")
        }

    })
}

// Fun para obtener un item por su ID
private fun searchItem() {
    API().getArticle("MLA918893329", object : Callback<Article> {
        override fun onResponse(call: Call<Article>, response: Response<Article>) {
            if (response.isSuccessful) {
                response.body()!!.apply {
                    println(this.title)
                }
            } else {
                println("Falló con código ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Article>, t: Throwable) {
            println("Falló al obtener el artículo :(")
        }

    })
}
