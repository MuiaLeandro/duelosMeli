package ar.teamrocket.duelosmeli.data.impl

import android.util.Log
import ar.teamrocket.duelosmeli.Game
import ar.teamrocket.duelosmeli.data.MeliRepository
import ar.teamrocket.duelosmeli.model.Article
import ar.teamrocket.duelosmeli.model.Articles
import ar.teamrocket.duelosmeli.model.Category
import ar.teamrocket.duelosmeli.service.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeliRepositoryImpl : MeliRepository {

    val categoriesCache: MutableList<Category> = mutableListOf()
    private val itemsCache = mutableMapOf<String, Articles>()

    // Se obtiene una lista de categorías
    override fun searchCategories(
        game: Game,
        callback: (List<Category>) -> Unit,
        onError: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {

        if (categoriesCache.isNotEmpty()) {
            callback(categoriesCache)
        } else {
            API().getCategories(object : Callback<List<Category>> {
                override fun onResponse(
                    call: Call<List<Category>>,
                    response: Response<List<Category>>
                ) {
                    if (response.isSuccessful) {
                        callback(response.body()!!)
                        categoriesCache.addAll(response.body()!!)
                    } else {
                        println("Falló con código ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    onFailure(t)
                }
            })
        }
    }

    // Se obtiene un item de una categoría
    override fun searchItemFromCategory(
        id: String, currentGame: Game, callback: (Articles) -> Unit, onError: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {

        if (itemsCache.containsKey(id)) {
            callback(itemsCache[id]!!)
        } else {
            API().getArticlesFromCategory(id, object : Callback<Articles> {
                override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                    if (response.isSuccessful) {
                        callback(response.body()!!)
                        itemsCache[id] = response.body()!!
                    } else {
                        println("Falló con código ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Articles>, t: Throwable) {
                    onFailure(t)
                }
            })
        }
    }
}