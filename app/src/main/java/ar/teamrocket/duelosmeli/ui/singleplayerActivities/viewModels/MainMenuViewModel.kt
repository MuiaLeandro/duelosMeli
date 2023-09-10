package ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.teamrocket.duelosmeli.httpStatusHandler
import ar.teamrocket.duelosmeli.data.model.CategoriesList
import ar.teamrocket.duelosmeli.data.model.Category
import ar.teamrocket.duelosmeli.data.model.Country
import ar.teamrocket.duelosmeli.data.model.Language
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * ViewModel del MainMenu (screen de selección de modos de juego)
 * @param [meliRepositoryImpl] clase que implementa la interfaz [MeliRepository]]
 * Por inyección de dependencias le pasamos [MeliRepositoryImpl], que hace las llamadas a las APIs
 */
class MainMenuViewModel(val meliRepositoryImpl : MeliRepository) : ViewModel() {

    val availableLanguage = Language.getAvailableLanguageCountry()
    val categoriesException = MutableLiveData<Throwable>()
    lateinit var categories: List<Category>

    /**
     * Llamamos a la API de categorias correspondiente al pais que consideramos para la
     * configuración de la app. Guardamos la lista de categorías en el Singleton [CategoriesList]
     */
    fun searchCategories() {
        viewModelScope.launch {
            try {
                categories = when (availableLanguage) {
                    Country.ARGENTINA -> meliRepositoryImpl.searchCategories()
                    Country.BRASIL -> meliRepositoryImpl.searchCategoriesBR()
                }

                CategoriesList.categoriesList = categories

            } catch (e: Exception) {
                when (e) {
                    is HttpException -> httpStatusHandler(e)
                    is UnknownHostException -> categoriesException.value = e
                }
            }
        }
    }
}
