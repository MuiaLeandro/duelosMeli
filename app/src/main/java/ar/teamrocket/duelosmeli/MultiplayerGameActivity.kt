package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGameBinding
import ar.teamrocket.duelosmeli.databinding.ActivityNewMultiplayerGameBinding
import ar.teamrocket.duelosmeli.ui.HomeActivity

class MultiplayerGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGuessed.setOnClickListener { viewMultiplayerGamePartialResultActivity() }
    }

    private fun viewMultiplayerGamePartialResultActivity() {
        val intent = Intent(this, MultiplayerGamePartialResultActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)

    }

}