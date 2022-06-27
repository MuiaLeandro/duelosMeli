package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityNewDuelBinding
import ar.teamrocket.duelosmeli.ui.MainMenuActivity

class NewDuelActivity : AppCompatActivity() {
    lateinit var binding :ActivityNewDuelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iHeader.tvTitle.text = "Nuevo duelo"
        binding.iHeader.ivButtonBack.setOnClickListener{ onBackPressed() }
        binding.btnStartDuel.setOnClickListener{ viewDuelActivity() }

        //TODO: Hacer logica para que se cree el QR con los items
    }

    private fun viewDuelActivity() {
        val intent = Intent(this, DuelActivity::class.java)
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
