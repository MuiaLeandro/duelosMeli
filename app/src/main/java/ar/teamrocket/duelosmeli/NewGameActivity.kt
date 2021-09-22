package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityNewGameBinding

class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStartGame.setOnClickListener { viewGame() }
    }

    private fun viewGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}