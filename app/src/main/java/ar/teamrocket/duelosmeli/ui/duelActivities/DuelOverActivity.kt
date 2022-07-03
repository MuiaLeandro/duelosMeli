package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.databinding.ActivityDuelOverBinding
import ar.teamrocket.duelosmeli.ui.MainMenuActivity

class DuelOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDuelOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuelOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pointsAchieved = intent.extras!!.getInt("Points")

        binding.iHeader.tvTitle.text = getString(R.string.partida_finalizada)
        achievedPointsBinder(pointsAchieved)

        binding.btnShare.setOnClickListener { share(pointsAchieved) }
        binding.btnTelegram.setOnClickListener { shareByTelegram(pointsAchieved) }
        binding.btnWhatsapp.setOnClickListener { shareByWhatsapp(pointsAchieved) }
        binding.btnBackToHome.setOnClickListener { viewMainMenu() }
        binding.iHeader.ivButtonBack.setOnClickListener{ onBackPressed() }
    }

    private fun share(pointsAchieved: Int) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "¡Terminé la partida!\nHice $pointsAchieved puntos.")
        }
        startActivity(intent)
    }

    private fun shareByTelegram(pointsAchieved: Int) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "¡Terminé la partida!\nHice $pointsAchieved puntos.")
            setPackage("org.telegram.messenger")
        }
        startActivity(intent)
    }

    private fun shareByWhatsapp(pointsAchieved: Int) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "¡Terminé la partida!\nHice $pointsAchieved puntos.")
            setPackage("com.whatsapp")
        }
        startActivity(intent)
    }

    private fun achievedPointsBinder(pointsAchieved: Int) {
        binding.tvScoreAchieved.text = getString(R.string.achieved_points, pointsAchieved)
    }

    private fun viewMainMenu() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}