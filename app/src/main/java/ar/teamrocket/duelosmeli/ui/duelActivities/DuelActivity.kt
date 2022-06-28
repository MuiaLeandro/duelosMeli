package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ar.teamrocket.duelosmeli.data.model.ItemDuel
import ar.teamrocket.duelosmeli.databinding.ActivityDuelBinding
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DuelActivity : AppCompatActivity() {
    lateinit var binding: ActivityDuelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gson = Gson()

        //TODO: Hacer logica para leer los productos de la activity anterior y presentarlos en pantalla
        val itemsString = intent.getStringExtra("ITEMS")
        val itemsType = object : TypeToken<MutableList<ItemDuel>>() {}.type
        val items: MutableList<ItemDuel> = gson.fromJson(itemsString, itemsType)
        Log.d("ITEMS",items.toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //TODO: ir al duel over?
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}