package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ar.teamrocket.duelosmeli.data.model.ItemDuel
import ar.teamrocket.duelosmeli.databinding.ActivityNewDuelBinding
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class NewDuelActivity : AppCompatActivity() {
    lateinit var binding :ActivityNewDuelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //TODO: Hacer logica para que se cree el QR con los items
        val gson = Gson()
        val items = mutableListOf<ItemDuel>()
        items.add(ItemDuel("item1","titulo1","4565","http://dsgfgd", listOf("7854","44")))
        items.add(ItemDuel("item2","titulo2","234","http://dsgfgd", listOf("56","775")))
        items.add(ItemDuel("item3","titulo3","908","http://dsgfgd", listOf("234","6789")))
        val itemsString: String = gson.toJson(items)

        binding.iHeader.tvTitle.text = "Nuevo duelo"
        binding.iHeader.ivButtonBack.setOnClickListener{ onBackPressed() }
        binding.btnStartDuel.setOnClickListener{ viewDuelActivity(itemsString) }

        //TODO: Obtener los items




        //TODO: Crear el QR con los items
        val barcodeEncoder = BarcodeEncoder()
        try {
            val bitmap = barcodeEncoder.encodeBitmap(itemsString,BarcodeFormat.QR_CODE,750,750)
            binding.ivQRCode.setImageBitmap(bitmap)
        } catch (e:Exception){
            Log.e("QR_CODE",e.toString())
        }
    }

    private fun viewDuelActivity(items:String) {
        val intent = Intent(this, DuelActivity::class.java)
        intent.putExtra("ITEMS",items)
        startActivity(intent)
        finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
