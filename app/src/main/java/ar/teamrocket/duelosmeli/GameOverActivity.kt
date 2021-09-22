package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBackToHome.setOnClickListener { viewNewGame() }

        val pointsAchieved = intent.extras?.getInt("Points")
        val pointsAchievedString = getString(R.string.puntosLogrados, pointsAchieved)
        val pointsHighscore = getString(R.string.puntosRecord, pointsAchieved)

        binding.tvScoreAchieved.text = pointsAchievedString
        binding.tvHigherScore.text = pointsHighscore
    }

    private fun viewNewGame() {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
    }
}