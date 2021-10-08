package ar.teamrocket.duelosmeli.data

import ar.teamrocket.duelosmeli.domain.model.Game
import ar.teamrocket.duelosmeli.domain.model.Article
import ar.teamrocket.duelosmeli.domain.model.Articles
import ar.teamrocket.duelosmeli.domain.model.Category

interface MeliRepository {

    fun searchCategories(
        game: Game,
        callback: (List<Category>) -> Unit,
        onError: (Int) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun searchItemFromCategory(
        id: String, currentGame: Game, callback: (Articles) -> Unit, onError: (Int) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun searchItem(
        id: String, callback: (Article) -> Unit, onError: (Int) -> Unit,
        onFailure: (Throwable) -> Unit
    )
}