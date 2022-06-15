package ar.teamrocket.duelosmeli.data.repository.impl

import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
import ar.teamrocket.duelosmeli.data.retrofit.API

class MeliRepositoryImpl : MeliRepository {

    // Se obtiene una lista de categorías
    override suspend fun searchCategories(): List<Category> {

        val response = API().getCategories()
        return response.body()!!
    }

    // Se obtiene un item de una categoría
    override suspend fun searchItemFromCategory(id: String): Articles {

        val response = API().getArticlesFromCategory(id)
        return response.body()!!
    }

    override suspend fun searchItemFromCategory(
        id: String,
        state: String,
        city: String
    ): Articles {
        val response = API().getArticlesFromCategory(id,state,city)
        return response.body()!!
    }

    // Se obtienen los datos más detallados de un artículo, por ahora usamos solo una imágen
    override suspend fun searchItem(id: String): Article {

        val response = API().getArticle(id)
        return response.body()!!
    }

    // Brazilian searchs
    // Se obtiene una lista de categorías
    override suspend fun searchCategoriesBR(): List<Category> {

        val response = API().getCategoriesBR()
        return response.body()!!
    }

    // Se obtiene un item de una categoría
    override suspend fun searchItemFromCategoryBR(id: String): Articles {

        val response = API().getArticlesFromCategoryBR(id)
        return response.body()!!
    }
}