package ar.teamrocket.duelosmeli.ui

import android.util.Log
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
import ar.teamrocket.duelosmeli.data.retrofit.API
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun main(args: Array<String>) {
    /*fun test(){
        var asd: MutableList<Int> = mutableListOf()

        asd.add(1)
        print(asd[0])
    }

    test()*/

    /*var lista: List<String> = listOf("1", "2", "3")
    var listaMutable: MutableList<String> = mutableListOf()

    listaMutable.add("Primero")
    listaMutable.add("Segundo")
    listaMutable.add("Tercero")

    println(listaMutable[listaMutable.lastIndex])
    println(lista[lista.lastIndex])*/


    //Hardcodeo una lista de categorias
    var categoria: MutableList<String> = mutableListOf()
    categoria.add("MLA5725")
    categoria.add("MLA1512")
    categoria.add("MLA1403")
    categoria.add("MLA1071")
    categoria.add("MLA1367")
    categoria.add("MLA1368")
    categoria.add("MLA1743")
    categoria.add("MLA1384")
    categoria.add("MLA1246")
    categoria.add("MLA1039")

    //Funcion para obtener una categoria random desde una lista
    fun randomCategory(categoriesList: List<String>): String {
        val randomNumber = (categoriesList.indices).random()
        return categoriesList[randomNumber]
    }
    val randomCategory = randomCategory(categoria)
    //println(randomCategory)



    //searchItem()
    //searchCategories()
    //searchItemFromCategory("1648")

    /* Tuve que inventar esta funcion que parece un choclo, es una combinacion entre
    *  searchCategories() y searchItemFromCategory(). Tuve que meter searchItemFromCategory()
    * adentro del onResponse de searchCategories() porque no puedo sacar un valor de retorno
    * de las funciones, sale tod0 como Unit, y por mas que use toString() se rompia.
    * Entonces primero se busca una categoria, la guardo en un val y ese val entra en la de
    * buscar el item. */
    // Tambien meti la funcion de buscar item por su id para obtener la imagen
    //searchCategoryRandomAndItemFromCategoryRandom()

    //val numeroRandom = (1..3).random()
    //println(numeroRandom)

    println("")
    var play = true
    while (play == true){
        println("Let's play")
        play = false
        println("Game over")
    }

    /*val timer = MiContador(30000, 1000)
    timer.start()

    class MiContador(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            //Lo que quieras hacer al finalizar
        }

        override fun onTick(millisUntilFinished: Long) {
            //texto a mostrar en cuenta regresiva en un textview
            //countdownText.setText(millisUntilFinished / 1000.toString() + "")
        }
    }*/

}




/*
fun searchCategoryRandomAndItemFromCategoryRandom() {
    fun searchCategories() {
        API().getCategories(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val categories = response.body()!!
                    val category = categories.get((categories.indices).random()).id
                    println("La categoría es: $category")

                    fun searchItemFromCategory(id: String) {
                        API().getArticlesFromCategory(id, object : Callback<Articles> {
                            override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                                if (response.isSuccessful) {
                                    response.body()!!.apply {
                                        var itemsList: MutableList<Article> = mutableListOf()
                                        itemsList.addAll(this.results)
                                        val item = itemsList.get((itemsList.indices).random())
                                        println("El artículo es: ${item.title}")
                                        println("Precio: ${item.price}")
                                        println("URL del thumbnail del producto: ${item.thumbnail}")
                                        val id = item.id

                                        // Esta es la funcion de buscar un item por su ID
                                        // La uso para obtener una picture, para que vean la diferencia
                                        // de calidad de la imagen comparado con el thumbnail
                                        fun searchItem(id:String) {
                                            API().getArticle(id, object : Callback<Article> {
                                                override fun onResponse(call: Call<Article>, response: Response<Article>) {
                                                    if (response.isSuccessful) {
                                                        response.body()!!.apply {
                                                            println("URL de la imagen del producto: ${this.pictures[0].secureUrl}")
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


// Fun para buscar items de una categoria
fun searchItemFromCategory(id: String) {
    API().getArticlesFromCategory(id, object : Callback<Articles> {
        override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
            if (response.isSuccessful) {
                response.body()!!.apply {
                    var itemsList: MutableList<Article> = mutableListOf()
                    itemsList.addAll(this.results)
                    println(itemsList.get((itemsList.indices).random()).title)
                    //println(itemsList.get((itemsList.indices).random()).price)
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

// Fun para buscar las categorias
fun searchCategories() {
    API().getCategories(object : Callback<List<Category>> {
        override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
            if (response.isSuccessful) {
                val categories = response.body()!!
                println(categories.get((categories.indices).random()).id)
            } else {
                println("Falló con código ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<Category>>, t: Throwable) {
            Log.e("Main", "Falló al obtener las categorias", t)
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
                    println(this.pictures[0].secureUrl)
                    println(this.thumbnail)
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
*/
