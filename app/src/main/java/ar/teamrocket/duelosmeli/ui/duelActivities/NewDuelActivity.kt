package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.model.ItemDuel
import ar.teamrocket.duelosmeli.databinding.ActivityNewDuelBinding
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels.GameViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException

class NewDuelActivity : AppCompatActivity() {
    lateinit var binding :ActivityNewDuelBinding
    private val vm: GameViewModel by viewModel()
    private val items = mutableListOf<ItemDuel>()
    private val gson = Gson()
    var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.clLoading.visibility = View.VISIBLE
        binding.btnStartDuel.visibility = View.GONE
        vm.findCategories()
        setObservers()

        binding.iHeader.tvTitle.text = "Nuevo duelo"
        binding.iHeader.ivButtonBack.setOnClickListener { onBackPressed() }
        binding.btnStartDuel.setOnClickListener { viewDuelActivity(listToString(items)) }
    }


    private fun setObservers(){
        vm.itemDuel.observe(this){
            if (counter < 5){
                items.add(it)
                Log.d(("Item" + counter.toString()), itemToString(it))
                vm.findCategories()
                counter++
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.pbDeterminateBarDuel.setProgress(counter * 20, true)
                }
            } else {
                Log.d("Items", listToString(items))
                QRGenerator(items)
                binding.clLoading.visibility = View.GONE
                binding.btnStartDuel.visibility = View.VISIBLE
            }
        }

        vm.categoriesException.observe(this, this::handleException)
        vm.itemFromCategoryException.observe(this, this::handleException)
        vm.itemException.observe(this, this::handleException)
    }


    private fun viewDuelActivity(items:String) {
        val intent = Intent(this, DuelActivity::class.java)
        intent.putExtra("ITEMS",items)
        startActivity(intent)
        finish()
    }

    fun QRGenerator(item: List<ItemDuel>){
        val barcodeEncoder = BarcodeEncoder()
        try {
             val bitmap = barcodeEncoder.encodeBitmap(listToString(item),BarcodeFormat
                 .QR_CODE,750,750)
            binding.ivQRCode.setImageBitmap(bitmap)
            Log.d("QR_CODE",listToString(item))
        } catch (e:Exception){
            Log.e("QR_CODE",e.toString())
        }
    }

    fun listToString(list: List<ItemDuel>): String{
        return gson.toJson(list)
    }

    fun itemToString(list: ItemDuel): String{
        return gson.toJson(list)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleException(exception: Throwable?) {
        if (exception is UnknownHostException)
        //Toast.makeText(this, "Ups! Connection lost :(", Toast.LENGTH_LONG).show()
        /*Snackbar.make(binding.root, "Ups! Connection lost :(", Snackbar.LENGTH_SHORT)
            .setTextColor(resources.getColor(R.color.black))
            .setBackgroundTint(resources.getColor(R.color.black))
            .show()*/
            MaterialAlertDialogBuilder(
                this,
                R.style.Dialog
            )
                .setTitle(R.string.conexion_perdida)
                .setMessage(R.string.asegurar_conexion)
                .setNegativeButton(R.string.terminar_partida) { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton(R.string.reintentar_conexion) { dialog, which ->
                    // Respond to positive button press
                    vm.findCategories()
                }
                .show()
    }
}
