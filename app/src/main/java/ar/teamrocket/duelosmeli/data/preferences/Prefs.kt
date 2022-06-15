package ar.teamrocket.duelosmeli.data.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Prefs(val context: Context) {
    companion object {
        val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"
    }
    private val IS_FIRST_USE = "FIRST_USE"
    private val IS_LOCATION_ENABLED = "IS_LOCATION_ENABLED"
    private val LOCATION_COUNTRY = "LOCATION_COUNTRY" // ARGENTINA
    private val LOCATION_ADMIN = "LOCATION_ADMIN" // PROVINCIA DE BUENOS AIRES (in MELI filter is "state" but BS. AS. esta dividida por zonas o "interior")
    private val LOCATION_SUB_ADMIN = "LOCATION_SUB_ADMIN" //LA MATANZA (in MELI filter is "city")
    private val LOCATION_STATE_ID = "LOCATION_STATE_ID"
    private val LOCATION_CITY_ID = "LOCATION_CITY_ID"

    private val storage = context.getSharedPreferences(SHARED_PREFERENCE_NAME,MODE_PRIVATE)

    fun saveIsFirstUse(isFirstUse : Boolean) {
        storage.edit().putBoolean(IS_FIRST_USE,isFirstUse).apply()
    }

    fun getIsFirstUse():Boolean{
        return storage.getBoolean(IS_FIRST_USE,true)
    }

    fun saveLocationEnabled(isLocationEnabled : Boolean) {
        storage.edit().putBoolean(IS_LOCATION_ENABLED,isLocationEnabled).apply()
    }
    fun getLocationEnabled():Boolean{
        return storage.getBoolean(IS_LOCATION_ENABLED,false)
    }
    fun saveLocationCountry(country : String) {
        storage.edit().putString(LOCATION_COUNTRY,country).apply()
    }
    fun getLocationCountry():String{
        return storage.getString(LOCATION_COUNTRY,"") ?: ""
    }
    fun saveLocationState(state : String) {
        storage.edit().putString(LOCATION_ADMIN,state).apply()
    }
    fun getLocationState():String{
        return storage.getString(LOCATION_ADMIN,"") ?: ""
    }
    fun saveLocationCity(subAdmin : String) {
        storage.edit().putString(LOCATION_SUB_ADMIN,subAdmin).apply()
    }
    fun getLocationCity():String{
        return storage.getString(LOCATION_SUB_ADMIN,"") ?: ""
    }
    fun saveLocationStateId(state : String) {
        storage.edit().putString(LOCATION_STATE_ID,state).apply()
    }
    fun getLocationStateId():String{
        return storage.getString(LOCATION_STATE_ID,"") ?: ""
    }
    fun saveLocationCityId(subAdmin : String) {
        storage.edit().putString(LOCATION_CITY_ID,subAdmin).apply()
    }
    fun getLocationCityId():String{
        return storage.getString(LOCATION_CITY_ID,"") ?: ""
    }

}