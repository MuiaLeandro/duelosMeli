package ar.teamrocket.duelosmeli.data.repository

import ar.teamrocket.duelosmeli.domain.Game
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category

interface MeliRepository {

    fun searchCategories(
        callback: (List<Category>) -> Unit,
        onError: (Int) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun searchItemFromCategory(
        id: String, callback: (Articles) -> Unit, onError: (Int) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun searchItem(
        id: String, callback: (Article) -> Unit, onError: (Int) -> Unit,
        onFailure: (Throwable) -> Unit
    )
}