package ar.teamrocket.duelosmeli.service

import ar.teamrocket.duelosmeli.model.Article
import ar.teamrocket.duelosmeli.model.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MercadoLibreApi {

    companion object {
        const val ARGENTINA: String = "MLA"
    }

    @GET("items/{itemId}")
    fun getArticle(@Path("itemId") id: String): Call<Article>

    @GET("sites/${ARGENTINA}/categories")
    fun getCategories(): Call<Category>

    @GET("items/{categoryId}")
    fun getArticlesFromCategory(id: String): Call<Article>
}