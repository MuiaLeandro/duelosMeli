package ar.teamrocket.duelosmeli.data

import android.app.Activity
import android.content.Context
import com.google.zxing.integration.android.IntentIntegrator

class QRScanner {

    fun initScanner(context: Activity) {
        val integrator = IntentIntegrator(context)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Scanea el QR de un amigo!")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }
}