package ar.teamrocket.duelosmeli.ui

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.teamrocket.duelosmeli.ui.duelActivities.NewDuelActivity
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.QRScanner
import ar.teamrocket.duelosmeli.data.preferences.Prefs
import ar.teamrocket.duelosmeli.databinding.ActivityMainMenuBinding
import ar.teamrocket.duelosmeli.httpStatusHandler
import ar.teamrocket.duelosmeli.isInternetAvailable
import ar.teamrocket.duelosmeli.ui.duelActivities.DuelActivity
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.views.NewGameActivity
import ar.teamrocket.duelosmeli.ui.multiplayerActivities.view.NewMultiplayerGameActivity
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels.MainMenuViewModel
import ar.teamrocket.duelosmeli.ui.userProfile.UserProfileActivity
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.*


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    private val viewModel: MainMenuViewModel by viewModel()
    val scanner = QRScanner()
    private val prefs: Prefs by inject()
    private val westCities = setOf("La Matanza", "Merlo", "Moreno", "Morón", "Gral. Rodríguez", "Marcos Paz", "Hurlingham", "Ituzaingó", "Tres de Febrero")
    private val southCities = setOf("Avellaneda", "Quilmes", "Berazategui", "Florencio Varela", "Lanús", "Lomas de Zamora", "Almirante Brown", "Esteban Echeverría", "Ezeiza", "Presidente Perón", "San Vicente")
    private val northCities = setOf("Vicente Lopéz", "San Isidro", "San Fernando", "Tigre", "General San Martín", "San Miguel", "José C. Paz", "Malvinas Argentinas" , "Escobar", "Pilar")
    private val costaAtlanticaCities = setOf("La Costa","Pinamar","Villa Gesell","Mar Chiquita","General Pueyrredón","General Alvarado","Lobería","Necochea","San Cayetano","Tres Arroyos","Coronel Dorrego","Monte Hermoso","Coronel Rosales","Bahía Blanca","Villarino","Patagones")

    companion object {
        val REQUEST_CODE_LOCATION = 0
    }
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if(prefs.getLocationEnabled()){
            binding.tvLocationPlay.text = prefs.getLocationState()
        } else {
            binding.tvLocationPlay.text=""
        }

        if(prefs.getIsFirstUse()){
            if (isInternetAvailable(this)) {
                showDialogForGameWithLocation()
                prefs.saveIsFirstUse(false)
            }
            else {
                prefs.saveIsFirstUse(true)
            }
        }

        binding()
        searchCategories()
        setObservers()
    }

    /**
     * Bindeamos todos los botones. Para los casos que necesitamos internet verificamos la conexión.
     * No permitimos avanzar al usuario en el flujo de la app más allá de esta activity si no
     * cuenta con internet en el dispositivo (con las funciones que no necesitan internet por el
     * momento si se puede interactuar).
     */
    private fun binding() {
        binding.btnSinglePlayer.setOnClickListener { if (isInternetAvailable(this)) viewNewGame() else searchCategories() }
        binding.btnMultiPlayer.setOnClickListener { if (isInternetAvailable(this)) viewNewMultiplayerGame() else searchCategories() }
        binding.btnDuelMode.setOnClickListener { if (isInternetAvailable(this)) showDialogForDuelMode() else searchCategories() }
        binding.btnUserProfile.setOnClickListener{ viewUserProfile() }
        binding.btnHowToPlay.setOnClickListener{ viewHowToPlayActivity() }
        binding.btnAbout.setOnClickListener {viewAboutUs() }
        binding.clLocationContainer.setOnClickListener { if (isInternetAvailable(this)) showDialogForGameWithLocation() else searchCategories() }
    }

    private fun setObservers() {
        searchCategoriesObserver()
    }

    private fun searchCategories() {
        viewModel.searchCategories()
    }

    /**
     * Se observa la MutableLiveData [categoriesException] del viewmodel [MainMenuViewModel]
     * Si tenemos el [UnknownHostException] se lanza la función que handlea la exepción.
     */
    private fun searchCategoriesObserver() {
        viewModel.categoriesException.observe(this, this::handleCategoriesException)
    }

    //TODO hacer un constructor sin titulo para este dialog. Extraer los strings. Agregar traducciones. Si se quieren dejar los comentarios de las funciones, documentarlo arriba de la función
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

    /*
    * getLocation() : Si los permisos de ubicacion no estan, los pide.
    * Si la ubicacion GPS o los datos moviles estan encendidos pide la última ubicación conocida. Si
    * esta existe la guarda en Prefs. De lo contrario, hace una request para obtener una ubicacion (newLocationData)
    * */

    @SuppressLint("MissingPermission")
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

    private fun getAddress(latitude: Double, longitude: Double): Address {
        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(latitude,longitude,1)
        return address!![0]
    }
    private fun getCountryName(latitude: Double, longitude: Double): String {
        return getAddress(latitude, longitude).countryName
    }
    private fun getStateName(latitude: Double, longitude: Double): String {
        return getAddress(latitude, longitude).adminArea
    }
    private fun getCityName(latitude: Double, longitude: Double): String {
        return getAddress(latitude, longitude).subAdminArea
    }

    @SuppressLint("MissingPermission")
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


    private fun isLocationEnabled(): Boolean {      //devuelve TRUE si el GPS o los datos moviles estan encendidos
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /*
    * requestLocationPermission(): Si los permisos fueron rechazados anteriormente pedimos al
    * usuario que los habilite manualmente. De lo contrario hacemos el request para que acepte los
    * permisos por primera vez.
    * */
    //TODO usariamos el constructor hecho para el modal de arriba. Extraer los strings. Agregar traducciones
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

    /*
    * onRequestPermissionsResult: Lo que sucede una vez que el usuario ACEPTO/RECHAZO los permisos.
    * Si los aceptó se guarda en prefs el modo de juego con ubicacion y se obtiene la ubicacion,
    * de lo contrario se muesatra un mensaje informativo.
    * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    //TODO usar el constructor completo. Extraer los strings. Agregar traducciones. Si se quieren dejar los comentarios de las funciones, documentarlo arriba de la función
    private fun showDialogForDuelMode(){
        MaterialAlertDialogBuilder(this,
        R.style.Dialog)
        // Seteo de título y descripción del Dialog
        .setTitle("Modo duelo")
        .setMessage("¿Querés crear una partida o unirte?")
        .setPositiveButton("Unirme") { _, _ ->
            //Mostrar lector de QR
            scanner.initScanner(this)
        }
        .setNegativeButton("Crear") { _, _ ->
            //Mostrar QR con lista de 5 productos ya cargados
            viewNewDuelActivity()
        }
        .show()
    }

    //Funcion que maneja el resultado del escaneo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data)
        if (result != null){
            if (result.contents == null){
                Toast.makeText(this, "No pude leer el QR", Toast.LENGTH_LONG).show()
            }else{
                //result.contents es quien contiene el resultado del QR scaneado
                viewNewDuel(result.contents)
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun viewNewDuel(items: String) {
        val intent = Intent(this, DuelActivity::class.java)
        intent.putExtra("ITEMS",items)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (!isLocationPermissionGranted()){
            //ya no esta habilitado el permiso:
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
        Toast.makeText(this, "¡Pronto tendremos un instructivo!", Toast.LENGTH_LONG).show()
        //TODO: Agregar instructivo del juego
//        val intent = Intent(this, HowToPlayActivity::class.java)
//        startActivity(intent)
//        finish()
    }

    private fun viewAboutUs() {
        Toast.makeText(this, "Todavía no podemos presentarnos", Toast.LENGTH_LONG).show()
        // TODO: Agregar AboutUsActivity
        //val intent = Intent(this, AboutUsActivity::class.java)
        //startActivity(intent)
        //finish()
    }

    private fun viewNewDuelActivity() {
        val intent = Intent(this, NewDuelActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Handlea las posibles exepciones para la búsqueda de categorías
     * @param [e] la exepción "per se"
     */
    private fun handleCategoriesException(e: Throwable?) {
        when (e) {
            is HttpException -> httpStatusHandler(e)
            is UnknownHostException -> handleUnknownHostException(e)
            else -> throw RuntimeException("Unexpected exception. Message: ${e?.message}. Cause: ${e?.cause}")
        }
    }

    /**
     * Handlea la exepción lanzada al intentar la pegada a la API de categorías sin contar con conexión a internet. Mostramos un dialog.
     * @param [e] la exepción "per se".
     * [negativeFun] es el botón "Salir". La función [finish] finaliza la activity y salimos del juego.
     * [positiveFun] es el botón "Reintentar conexión". Se reintenta ejecutar [searchCategories]
     * Si no se consiguen traer las categorías por el error de conexión se vuelve a entrar a este flujo.
     */
    private fun handleUnknownHostException(e: Throwable?) {
        GenericMaterialDialog(
            this,
            R.string.sin_internet,
            R.string.revisar_conexion,
            R.string.salir,
            R.string.reintentar_conexion,
            negativeFun = { finish() },
            positiveFun =  { searchCategories() })
            .buildDialog()
    }
}
