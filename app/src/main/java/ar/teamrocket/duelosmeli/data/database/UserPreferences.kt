package ar.teamrocket.duelosmeli.data.database

import android.content.Context

class UserPreferences(val context: Context) {

    val SHARED_NAME = "UserDataBase"
    val SHARED_USER_NAME = "userName"
    val SHARED_USER_PHOTO = "userPhoto"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveName(name: String) {
        storage.edit().putString(SHARED_USER_NAME, name).apply()
    }

    fun savePhoto(photo: String) {
        storage.edit().putString(SHARED_USER_PHOTO, photo).apply()
    }

    fun getName() = storage.getString(SHARED_USER_NAME, "")!!

    fun getPhoto() = storage.getString(SHARED_USER_PHOTO, "")!!
}