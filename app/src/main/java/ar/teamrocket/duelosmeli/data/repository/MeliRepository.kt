package ar.teamrocket.duelosmeli.data.repository

import ar.teamrocket.duelosmeli.domain.Game
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category

interface MeliRepository {

    suspend fun searchCategories(): List<Category>

    suspend fun searchItemFromCategory(id: String): Articles

    suspend fun searchItem(id: String): Article
}