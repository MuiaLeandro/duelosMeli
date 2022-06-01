package ar.teamrocket.duelosmeli.data.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Prefs(val context: Context) {
    companion object {
        val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"
    }
    private val IS_FIRST_USE = "FIRST_USE"
    private val IS_LOCATION_ENABLED = "IS_LOCATION_ENABLED"

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

}