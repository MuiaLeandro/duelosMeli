package ar.teamrocket.duelosmeli.data.repository.impl

import ar.teamrocket.duelosmeli.domain.Game
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
import ar.teamrocket.duelosmeli.data.retrofit.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeliRepositoryImpl : MeliRepository {

    val categoriesCache: MutableList<Category> = mutableListOf()
    private val itemsCache = mutableMapOf<String, Articles>()
    private val detailedItemsCache = mutableMapOf<String, Article>()

    // Se obtiene una lista de categorías
    override fun searchCategories(
        callback: (List<Category>) -> Unit,
        onError: (Int) -> Unit,
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
                    when (response.code()) {
                        in 200..299 -> {
                            callback(response.body()!!)
                            categoriesCache.addAll(response.body()!!)
                        }
                        400 -> onError(R.string.bad_request)
                        404 -> onError(R.string.resource_not_found)
                        in 500..599 -> onError(R.string.server_error)
                        else -> onError(R.string.unknown_error)
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
        id: String, callback: (Articles) -> Unit, onError: (Int) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {

        if (itemsCache.containsKey(id)) {
            callback(itemsCache[id]!!)
        } else {
            API().getArticlesFromCategory(id, object : Callback<Articles> {
                override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                    when (response.code()) {
                        in 200..299 -> {
                            callback(response.body()!!)
                            itemsCache[id] = response.body()!!
                        }
                        400 -> onError(R.string.bad_request)
                        404 -> onError(R.string.resource_not_found)
                        in 500..599 -> onError(R.string.server_error)
                        else -> onError(R.string.unknown_error)
                    }
                }

                override fun onFailure(call: Call<Articles>, t: Throwable) {
                    onFailure(t)
                }
            })
        }
    }

    // Se obtienen los datos más detallados de un artículo, por ahora usamos solo una imágen
    override fun searchItem(
        id: String, callback: (Article) -> Unit, onError: (Int) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {

        if (detailedItemsCache.containsKey(id)) {
            callback(detailedItemsCache[id]!!)
        } else {
            API().getArticle(id, object : Callback<Article> {
                override fun onResponse(call: Call<Article>, response: Response<Article>) {
                    when (response.code()) {
                        in 200..299 -> {
                            callback(response.body()!!)
                            detailedItemsCache[id] = response.body()!!
                        }
                        400 -> onError(R.string.bad_request)
                        404 -> onError(R.string.resource_not_found)
                        in 500..599 -> onError(R.string.server_error)
                        else -> onError(R.string.unknown_error)
                    }
                }

                override fun onFailure(call: Call<Article>, t: Throwable) {
                    onFailure(t)
                }
            })
        }
    }
}