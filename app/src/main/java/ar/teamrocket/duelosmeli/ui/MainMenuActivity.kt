package ar.teamrocket.duelosmeli.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.preferences.Prefs
import ar.teamrocket.duelosmeli.databinding.ActivityMainMenuBinding
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.views.NewGameActivity
import ar.teamrocket.duelosmeli.ui.multiplayerActivities.view.NewMultiplayerGameActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    private val prefs: Prefs by inject()
    companion object {
        val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(prefs.getIsFirstUse()){
            showDialogForGameWithLocation()
            prefs.saveIsFirstUse(false)
        }
        binding.btnOnePlayer.setOnClickListener { viewNewGame() }
        binding.btnMultiplayer.setOnClickListener { viewNewMultiplayerGame() }
        binding.btnOptions.setOnClickListener { showDialogForGameWithLocation() }
    }

    private fun showDialogForGameWithLocation(){
        MaterialAlertDialogBuilder(this,
            R.style.Dialog)
            .setTitle("")
            .setMessage("¿Deseas jugar con productos que se venden cerca tuyo?")
            .setNegativeButton("No") { _, _ ->
                prefs.saveLocationEnabled(false) //Setea configuracion para jugar sin location
                binding.tvLocationPlay.text="Jugando con ubicacion: NO"

            }
            .setPositiveButton("Si") { _, _ ->
                enabledLocation() //get location permission
            }
            .show()
    }

    private fun isLocationPermissionGranted()=
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun enabledLocation(){
        if(!isLocationPermissionGranted()){
            requestLocationPermission()
        } else {
            prefs.saveLocationEnabled(true)  //Setea configuracion para jugar con location
            binding.tvLocationPlay.text="Jugando con ubicacion: SI"

        }
    }

    private fun requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            Toast.makeText(this, "Acepta el permiso de ubicacion desde los ajustes de tu telefono movil", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_LOCATION )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //El permiso fue concedido ---> se guarda la configuracion para jugar con location
                prefs.saveLocationEnabled(true)
                binding.tvLocationPlay.text="Jugando con ubicacion: SI"
            } else {
                Toast.makeText(this, "Para poder utilizar tu ubicacion necesitamos que aceptes el permiso desde los ajustes de tu telefono movil", Toast.LENGTH_SHORT).show()
            }
            else -> { }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isLocationPermissionGranted()){
            //ya no esta habilitado el permiso:
            prefs.saveLocationEnabled(false)
            binding.tvLocationPlay.text="Jugando con ubicacion: NO"
            //Toast.makeText(this, "Ve a los ajustes de tu telefono movil para habilitar los permisos de ubicacion si deseas seguir jugando con productos que se venden cerca tuyo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun viewNewGame() {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun viewNewMultiplayerGame() {
        val intent = Intent(this, NewMultiplayerGameActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}