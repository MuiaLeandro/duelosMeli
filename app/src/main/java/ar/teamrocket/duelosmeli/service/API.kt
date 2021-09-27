package ar.teamrocket.duelosmeli.service

import ar.teamrocket.duelosmeli.model.Article
import ar.teamrocket.duelosmeli.model.Articles
import ar.teamrocket.duelosmeli.model.Category
import com.google.gson.Gson
import retrofit2.Callback
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

    fun getArticle(id: String, callback: Callback<Article>) {
        getAPI().getArticle(id).enqueue(callback)
    }

    fun getCategories(callback: Callback<List<Category>>){
        getAPI().getCategories().enqueue(callback)
    }

    fun getArticlesFromCategory(id: String, callback: Callback<Articles>){
        getAPI().getArticlesFromCategory(id).enqueue(callback)
    }
}