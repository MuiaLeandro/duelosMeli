package ar.teamrocket.duelosmeli

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.teamrocket.duelosmeli.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPlayGame.setOnClickListener { viewNewGame() }
    }

    fun viewNewGame() {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
        finish()
    }

}