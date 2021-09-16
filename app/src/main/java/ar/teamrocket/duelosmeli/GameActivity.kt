package ar.teamrocket.duelosmeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}