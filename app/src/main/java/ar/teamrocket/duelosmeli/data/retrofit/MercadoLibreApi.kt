package ar.teamrocket.duelosmeli.data.retrofit

import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
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

    @GET("sites/${ARGENTINA}/search")
    fun getArticlesFromCategory(@Query("category")id: String): Call<Articles>
}