package ar.teamrocket.duelosmeli.ui.singleplayerActivities.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.database.Player
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.databinding.ActivityGameOverBinding
import ar.teamrocket.duelosmeli.domain.GameFunctions
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.adapters.HighScoreAdapter
import org.koin.android.ext.android.inject


class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding
    private val playerDao : PlayerDao by inject()
    private val gameFunctions: GameFunctions by inject()
    //TODO: Agregar ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iHeader.tvTitle.text=getString(R.string.partida_finalizada)
        binding.iHeader.ivButtonBack.setOnClickListener { onBackPressed() }
        binding.btnBackToHome.setOnClickListener { viewNewGame() }

        val topTenPlayers = playerDao.getTopTenOrderByScore()

        val idPlayer = intent.extras!!.getLong("IdPlayer")
        //obtener jugador
        //val num: Long = 1
        var player:List<Player> = emptyList()

        if (idPlayer > 0) {
            player = playerDao.getById(idPlayer)
        }
        //playerDao.updatePlayer(player[0])
        val pointsAchieved = intent.extras!!.getInt("Points")
        val zeroPointAchievedString = getString(R.string.achieved_no_point)
        val onePointAchievedString = getString(R.string.achieved_one_point, pointsAchieved)
        val pointsAchievedString = getString(R.string.achieved_points, pointsAchieved)
        val pointsHighscore = getString(R.string.record_points, player[0].score)

        achievedPointsBinder(pointsAchieved, zeroPointAchievedString, onePointAchievedString, pointsAchievedString)
        binding.tvHigherScore.text = pointsHighscore

        // Seteo de botones
        goMenuOfGameSelection()
        playAgain(idPlayer)

        //Highscore RecyclerView
        binding.rvScoreTable.layoutManager = LinearLayoutManager(this)
        binding.rvScoreTable.adapter = HighScoreAdapter(topTenPlayers)
    }

    override fun onResume() {
        super.onResume()
        gameFunctions.audioPlayer(this, R.raw.gameover)
    }

    private fun achievedPointsBinder(pointsAchieved: Int, zero: String, one: String, moreThanOne: String) {
        when(pointsAchieved) {
            0 -> binding.tvScoreAchieved.text = zero
            1 -> binding.tvScoreAchieved.text = one
            else -> binding.tvScoreAchieved.text = moreThanOne
        }
    }

    // Botón para volver al inicio
    private fun goMenuOfGameSelection() {
        binding.btnHome.setOnClickListener {
            it.context
                .startActivity(Intent(this, MainMenuActivity::class.java))
            finish()
        }
    }

    // Botón para jugar de nuevo
    private fun playAgain(id:Long) {
        binding.btnPlay.setOnClickListener {
            it.context
                .startActivity(Intent(binding.root.context, GameActivity::class.java)
                    .putExtra("Id",id))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewNewGame()
    }

    private fun viewNewGame() {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
        finish()
    }
}