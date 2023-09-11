package ar.teamrocket.duelosmeli

import android.util.Log
import retrofit2.HttpException

/**
 * Función de nivel superior. Puede ser usada en cualquier parte del código.
 * La idea es poder handlear los codigos de status HTTP. Por el momento los dejamos con Logs.
 */
fun httpStatusHandler(e: HttpException) {
    when (e.code()) {
        in 100..199 -> Log.i("Informational", "Status code ${e.code()}. Response: ${e.response()}")
        in 200..299 -> Log.i("Success", "Status code ${e.code()}. Response: ${e.response()}")
        in 300..399 -> Log.i("Redirection", "Status code ${e.code()}. Response: ${e.response()}")
        in 400..499 -> Log.i("Client Error", "Status code ${e.code()}. Response: ${e.response()}")
        in 500..599 -> Log.i("Server Error", "Status code ${e.code()}. Response: ${e.response()}")
    }
}
