package ar.teamrocket.duelosmeli.ui.singleplayerActivities.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.databinding.ActivityGameOverBinding
import ar.teamrocket.duelosmeli.domain.GameFunctions
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.adapters.HighScoreAdapter
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels.GameOverViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding
    private val gameFunctions: GameFunctions by inject()
    private val vm: GameOverViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val idPlayer = intent.extras!!.getLong("IdPlayer")
        val pointsAchieved = intent.extras!!.getInt("Points")
        vm.setupTopTenPlayers()
        vm.setupCurrentPlayer(idPlayer)
        setListener(idPlayer)
        setUI(pointsAchieved)
    }

    private fun setUI(pointsAchieved: Int) {
        binding.iHeader.tvTitle.text = getString(R.string.partida_finalizada)
        achievedPointsBinder(pointsAchieved)
        setObservers()
    }

    private fun setObservers() {
        vm.topTenPlayers.observe(this) {
            it?.let {
                binding.rvScoreTable.layoutManager = LinearLayoutManager(this)
                binding.rvScoreTable.adapter = HighScoreAdapter(it)
            }
        }
        vm.currentPlayer.observe(this) {
            it?.let {
                binding.tvHigherScore.text = getString(R.string.record_points, it.score)
            }
        }
    }

    private fun setListener(idPlayer: Long) {
        goMenuOfGameSelection()
        playAgain(idPlayer)

        binding.iHeader.ivButtonBack.setOnClickListener { onBackPressed() }
        binding.btnBackToHome.setOnClickListener { viewNewGame() }
    }

    override fun onResume() {
        super.onResume()
        gameFunctions.audioPlayer(this, R.raw.gameover)
    }

    private fun achievedPointsBinder(
        pointsAchieved: Int
    ) {
        val zeroPointAchievedString = getString(R.string.achieved_no_point)
        val onePointAchievedString = getString(R.string.achieved_one_point, pointsAchieved)
        val pointsAchievedString = getString(R.string.achieved_points, pointsAchieved)

        binding.tvScoreAchieved.text = when (pointsAchieved) {
            0 -> zeroPointAchievedString
            1 -> onePointAchievedString
            else -> pointsAchievedString
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
    private fun playAgain(id: Long) {
        binding.btnPlay.setOnClickListener {
            it.context
                .startActivity(
                    Intent(binding.root.context, GameActivity::class.java)
                        .putExtra("Id", id)
                )
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