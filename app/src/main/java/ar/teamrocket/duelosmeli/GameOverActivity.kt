package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBackToHome.setOnClickListener { viewNewGame() }

        val pointsAchieved = intent.extras?.getInt("Points")
        binding.tvScoreAchieved.text = "Lograste ${pointsAchieved.toString()} puntos"
        binding.tvHigherScore.text = "Tu mayor puntaje: ${pointsAchieved.toString()}"
    }

    fun viewNewGame() {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
    }
}