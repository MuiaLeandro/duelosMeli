package ar.teamrocket.duelosmeli.data.model

/**
 * Este Singleton va a mantener la lista de categorias durante todo_ el ciclo de vida de la app.
 * Nos permite poder usar la lista en cualquier modo de juego,
 */
object CategoriesList {
    lateinit var categoriesList: List<Category>
}
