package ar.teamrocket.duelosmeli.data.service

import ar.teamrocket.duelosmeli.domain.model.Article
import ar.teamrocket.duelosmeli.domain.model.Articles
import ar.teamrocket.duelosmeli.domain.model.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadoLibreApi {

    companion object {
        const val ARGENTINA: String = "MLA"
    }

    @GET("items/{itemId}")
    fun getArticle(@Path("itemId") id: String): Call<Article>

    @GET("sites/${ARGENTINA}/categories")
    fun getCategories(): Call<List<Category>>

    /*@GET("items/{categoryId}")
    fun getArticlesFromCategory(@Path("categoryId")id: String): Call<Articles>*/

    @GET("sites/${ARGENTINA}/search")
    fun getArticlesFromCategory(@Query("category")id: String): Call<Articles>
}