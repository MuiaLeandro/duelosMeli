package ar.teamrocket.duelosmeli.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.preferences.Prefs
import ar.teamrocket.duelosmeli.databinding.ActivityMainMenuBinding
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.views.NewGameActivity
import ar.teamrocket.duelosmeli.ui.multiplayerActivities.view.NewMultiplayerGameActivity
import ar.teamrocket.duelosmeli.ui.userProfile.UserProfileActivity
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import java.io.IOException
import java.util.*


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    private val prefs: Prefs by inject()
    private val westCities = setOf("La Matanza", "Merlo", "Moreno", "Morón", "Gral. Rodríguez", "Marcos Paz", "Hurlingham", "Ituzaingó", "Tres de Febrero")
    private val southCities = setOf("Avellaneda", "Quilmes", "Berazategui", "Florencio Varela", "Lanús", "Lomas de Zamora", "Almirante Brown", "Esteban Echeverría", "Ezeiza", "Presidente Perón", "San Vicente")
    private val northCities = setOf("Vicente Lopéz", "San Isidro", "San Fernando", "Tigre", "General San Martín", "San Miguel", "José C. Paz", "Malvinas Argentinas" , "Escobar", "Pilar")
    private val costaAtlanticaCities = setOf("La Costa","Pinamar","Villa Gesell","Mar Chiquita","General Pueyrredón","General Alvarado","Lobería","Necochea","San Cayetano","Tres Arroyos","Coronel Dorrego","Monte Hermoso","Coronel Rosales","Bahía Blanca","Villarino","Patagones")

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if(prefs.getLocationEnabled()){
            binding.tvLocationPlay.text = prefs.getLocationState()
        } else {
            binding.tvLocationPlay.text=""
        }

        if(prefs.getIsFirstUse()){
            showDialogForGameWithLocation()
            prefs.saveIsFirstUse(false)
        }

        binding.btnSinglePlayer.setOnClickListener { viewNewGame() }
        binding.btnMultiPlayer.setOnClickListener { viewNewMultiplayerGame() }
        binding.btnUserProfile.setOnClickListener{ viewUserProfile() }
        binding.btnHowToPlay.setOnClickListener{ viewHowToPlayActivity() }
        binding.btnAbout.setOnClickListener {viewAboutUs() }
        binding.clLocationContainer.setOnClickListener { showDialogForGameWithLocation() }
    }

    private fun showDialogForGameWithLocation(){
        MaterialAlertDialogBuilder(this,
            R.style.Dialog)
            .setTitle("")
            .setMessage("¿Deseas jugar con productos que se venden cerca tuyo?")
            .setNegativeButton("No") { _, _ ->
                prefs.saveLocationEnabled(false) //Setea configuracion para jugar sin location
                binding.tvLocationPlay.text=""
            }
            .setPositiveButton("Si") { _, _ ->
                getLocation() // obtiene ubicacion
            }
            .show()
    }

    private fun isLocationPermissionGranted()=
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
/**
 * Si los permisos de ubicacion no estan, los pide.
 * Si la ubicacion GPS o los datos moviles estan encendidos pide la última ubicación conocida. Si
 * esta existe la guarda en Prefs. De lo contrario, hace una request para obtener una ubicacion (newLocationData)
 */
    private fun getLocation(){
        if(!isLocationPermissionGranted()){
            requestLocationPermission()
        } else if (isLocationEnabled()) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if(location == null){
                    // si no existe una ultima ubicacion conocida entonces la creamos con un request:
                    newLocationData()
                }else{
                    saveLocationWithinPrefs(location)

                    binding.tvLocationPlay.text = prefs.getLocationState()
                    Log.d("Coordenadas:" , "Your Location:${location.longitude},${location.latitude}")
                }
                prefs.saveLocationEnabled(true)  //Setea configuracion del juego para jugar con location
                binding.tvLocationPlay.text= prefs.getLocationState()
            }
        } else {
            Toast.makeText(this,"Por favor, para jugar con publicaciones cercanas debes activar la ubicación",Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLocationWithinPrefs(location: Location) {
        prefs.saveLocationCountry(getCountryName(location.latitude, location.longitude))
        prefs.saveLocationState(getStateName(location.latitude, location.longitude))
        prefs.saveLocationCity(getCityName(location.latitude, location.longitude))

        if (prefs.getLocationState() == "Provincia de Buenos Aires") {
            when {
                westCities.contains(prefs.getLocationCity()) -> prefs.saveLocationState("Bs.As. G.B.A. Oeste")
                southCities.contains(prefs.getLocationCity()) -> prefs.saveLocationState("Bs.As. G.B.A. Sur")
                northCities.contains(prefs.getLocationCity()) -> prefs.saveLocationState("Bs.As. G.B.A. Norte")
                costaAtlanticaCities.contains(prefs.getLocationCity()) -> prefs.saveLocationState("Bs.As. Costa Atlántica")
                else -> prefs.saveLocationState("Buenos Aires Interior")
            }
        } else {
            if (prefs.getLocationState() == "Buenos Aires") prefs.saveLocationState("Capital Federal")
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(this, Locale.getDefault())
        val address: MutableList<Address>
        try {
            address = geocoder.getFromLocation(latitude,longitude,1)
        } catch (e: IOException) {
            Log.e("LOCATION",e.message.toString())
            return null
        }
        return if (!address.isNullOrEmpty()) {
            address[0]
        } else {
            Toast.makeText(this, "Por favor, comprueba tu conexión a internet y vuelve a intentarlo", Toast.LENGTH_LONG).show()
            null
        }
    }
    private fun getCountryName(latitude: Double, longitude: Double): String {
        return getAddress(latitude, longitude)?.countryName ?: ""
    }
    private fun getStateName(latitude: Double, longitude: Double): String {
        return getAddress(latitude, longitude)?.adminArea ?: ""
    }
    private fun getCityName(latitude: Double, longitude: Double): String {
        return getAddress(latitude, longitude)?.subAdminArea ?: ""
    }

    private fun newLocationData() {
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private fun isLocationEnabled(): Boolean {
        //devuelve TRUE si el GPS o los datos moviles estan encendidos
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
    * Si los permisos fueron rechazados anteriormente pedimos al
    * usuario que los habilite manualmente. De lo contrario hacemos el request para que acepte los
    * permisos por primera vez.
    * */
    private fun requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            MaterialAlertDialogBuilder(this,
                R.style.Dialog)
                .setTitle("")
                .setMessage("Acepta el permiso de ubicacion desde los ajustes de tu telefono movil")
                .setNegativeButton("Cancelar") { _, _ -> }
                .setPositiveButton("Configuración del dispositivo") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_LOCATION )
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /* Si el usuario ACEPTÓ los permisos se guarda en prefs el modo de juego con ubicacion y se obtiene la ubicacion,
         * de lo contrario se muesatra un mensaje informativo.
         * */
        when (requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                prefs.saveLocationEnabled(true)
                binding.tvLocationPlay.text=""
                getLocation()
            } else {
                Toast.makeText(this, "Para poder utilizar tu ubicacion necesitamos que aceptes el permiso desde los ajustes de tu telefono movil", Toast.LENGTH_SHORT).show()
            }
            else -> { }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isLocationPermissionGranted()){
            // si ya no esta habilitado el permiso:
            prefs.saveLocationEnabled(false)
            binding.tvLocationPlay.text=""
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

    private fun viewUserProfile() {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun viewHowToPlayActivity() {
        val intent = Intent(this, HowToPlayActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun viewAboutUs() {

        Toast.makeText(this, "Todavía no podemos presentarnos", Toast.LENGTH_LONG).show()

        // Agregar AboutUsActivity
        //val intent = Intent(this, AboutUsActivity::class.java)
        //startActivity(intent)
        //finish()
    }

}