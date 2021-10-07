package ar.teamrocket.duelosmeli.data

import ar.teamrocket.duelosmeli.Game
import ar.teamrocket.duelosmeli.model.Category

interface MeliRepository {

    fun searchCategories(game: Game,
                         callback: (List<Category>) -> Unit,
                         onError: () -> Unit,
                         onFailure: (Throwable) -> Unit)
}