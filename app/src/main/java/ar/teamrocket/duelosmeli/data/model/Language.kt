package ar.teamrocket.duelosmeli.data.model

import java.util.*

/**
 * En principio vamos a manejar un solo idioma en la app (entre español o portugues)
 * que se va a determinar de acuerdo al idioma que tenga configurado el usuario
 * en su dispositivo (como viene siendo hasta ahora), pero para más adelante
 * podríamos permitir que la configuración del idioma se maneje desde la app, y con este
 * Singleton lo hacemos escalable a esa posibilidad.
 */
object Language {

    val language: Locale = Locale.getDefault()

    /**
     * En base al [language] que tenga configurado el usuario en su dispositivo, tenemos el país
     * por el cual vamos a tener en cuenta las configuraciones de traducción y APIs
     * @return [Country] se retorna un país de entre los que tenemos en el enum
     */
    fun getAvailableLanguageCountry(): Country{
        return when (language.toLanguageTag()) {
            "pt-BR", "pt-PT" -> Country.BRASIL
            else -> Country.ARGENTINA
        }
    }
}

/**
 * Paises que estamos teniendo en cuenta para las configuraciones de la app
 */
enum class Country {
    ARGENTINA, BRASIL
}
