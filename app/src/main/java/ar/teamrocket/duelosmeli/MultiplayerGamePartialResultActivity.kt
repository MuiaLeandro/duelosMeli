package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGamePartialResultBinding
import ar.teamrocket.duelosmeli.databinding.ActivityNewMultiplayerGameBinding

class MultiplayerGamePartialResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerGamePartialResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerGamePartialResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)

    }

}