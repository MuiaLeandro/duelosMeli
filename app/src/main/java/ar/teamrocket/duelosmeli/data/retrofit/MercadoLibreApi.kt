package ar.teamrocket.duelosmeli.data.retrofit

import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadoLibreApi {

    companion object {
        const val ARGENTINA: String = "MLA"
    }

    @GET("items/{itemId}")
    suspend fun getArticle(@Path("itemId") id: String): Response<Article>

    @GET("sites/${ARGENTINA}/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("sites/${ARGENTINA}/search")
    suspend fun getArticlesFromCategory(@Query("category")id: String): Response<Articles>
}