package ar.teamrocket.duelosmeli.data.retrofit

import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API {

    private fun getAPI(): MercadoLibreApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        return retrofit.create(MercadoLibreApi::class.java)
    }

    suspend fun getArticle(id: String): Response<Article> {
        return getAPI().getArticle(id)
    }

    suspend fun getCategories(): Response<List<Category>>{
        return getAPI().getCategories()
    }

    suspend fun getArticlesFromCategory(id: String): Response<Articles>{
        return getAPI().getArticlesFromCategory(id)
    }

    suspend fun getArticlesFromCategory(id: String,state: String,city:String): Response<Articles>{
        return getAPI().getArticlesFromCategory(id,state,city)
    }

    // Brazilian searchs
    suspend fun getCategoriesBR(): Response<List<Category>>{
        return getAPI().getCategoriesBR()
    }

    suspend fun getArticlesFromCategoryBR(id: String): Response<Articles>{
        return getAPI().getArticlesFromCategoryBR(id)
    }
}