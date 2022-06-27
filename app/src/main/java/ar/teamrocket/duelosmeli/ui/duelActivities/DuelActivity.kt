package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityDuelBinding
import ar.teamrocket.duelosmeli.ui.MainMenuActivity

class DuelActivity : AppCompatActivity() {
    lateinit var binding: ActivityDuelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: Hacer logica para leer los productos de la activity anterior y presentarlos en pantalla
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //TODO: ir al duel over?
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}