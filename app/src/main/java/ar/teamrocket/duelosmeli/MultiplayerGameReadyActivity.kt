package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGameBinding
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGameReadyBinding
import ar.teamrocket.duelosmeli.databinding.ActivityNewMultiplayerGameBinding
import ar.teamrocket.duelosmeli.ui.HomeActivity

class MultiplayerGameReadyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerGameReadyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerGameReadyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMultiplayer.setOnClickListener { viewMultiplayerGameActivity() }
    }

    private fun viewMultiplayerGameActivity() {
        val intent = Intent(this, MultiplayerGameActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, NewMultiplayerGameActivity::class.java)
        startActivity(intent)

    }

}