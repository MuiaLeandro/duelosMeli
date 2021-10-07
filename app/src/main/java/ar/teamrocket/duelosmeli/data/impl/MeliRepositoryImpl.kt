package ar.teamrocket.duelosmeli.data.impl

import android.util.Log
import ar.teamrocket.duelosmeli.Game
import ar.teamrocket.duelosmeli.data.MeliRepository
import ar.teamrocket.duelosmeli.model.Category
import ar.teamrocket.duelosmeli.service.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeliRepositoryImpl : MeliRepository {

    override fun searchCategories(
        game: Game,
        callback: (List<Category>) -> Unit,
        onError: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        API().getCategories(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                } else {
                    println("Falló con código ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}