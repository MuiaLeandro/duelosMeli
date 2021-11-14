package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGamePartialResultBinding
import ar.teamrocket.duelosmeli.domain.MultiplayerScoreAdapter
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer
import ar.teamrocket.duelosmeli.ui.viewmodels.MultiplayerGamePartialResultActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MultiplayerGamePartialResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerGamePartialResultBinding
    private val vm: MultiplayerGamePartialResultActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerGamePartialResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var playersOrderByScore = emptyList<Multiplayer>()
        if (vm.getAllMultiplayerOrderByScore().value != null) {
            playersOrderByScore = vm.getAllMultiplayerOrderByScore().value!!
        }

        val game = intent.extras!!.getParcelable<GameMultiplayer>("Game")!!
        val addPoint = intent.extras!!.getBoolean("AddPoint")

        vm.setGame(game)
        vm.setListMultiplayers()
        vm.setAllMultiplayerOrderByScore()
        vm.setCurrentPlayer()
        vm.setAddPoint(addPoint)
        if (vm.currentPlayer.value != null) {
            binding.tvCurrentNamePlayer.text = vm.currentPlayer.value!!.name
        }

        val adapter = MultiplayerScoreAdapter(playersOrderByScore)
        binding.rvScoreTableMultiplayer.layoutManager = LinearLayoutManager(this)
        binding.rvScoreTableMultiplayer.adapter = adapter

        setListeners(addPoint)
        setObservers(adapter, game)

    }

    private fun setObservers(adapter: MultiplayerScoreAdapter, game: GameMultiplayer) {
        vm.playersOrderByScore.observe(this, {
            if (it != null) {
                adapter.setListData(it)
            }
        })

        vm.team.observe(this,  {
            if (it != null) {
                binding.tvCurrentNamePlayer.text= vm.team.value!![game.currentPlayer].name
            }
        })


        vm.addPoint.observe(this, {
            if (it == true) {
                binding.tvPlayerSituation.text = getString(R.string.guessed)
            } else if (it == false) {
                binding.tvPlayerSituation.text = getString(R.string.did_not_guessed)
            }
        })
    }

    private fun setListeners(addPoint:Boolean) {
        vm.setAllMultiplayerOrderByScore()
        vm.setListMultiplayers()
        vm.setCurrentPlayer()
        vm.setAddPoint(addPoint)
        binding.btnNext.setOnClickListener {
            nextView()
        }
    }

    private fun nextView() {
        var game1: GameMultiplayer? = vm.game.value
        if (game1 != null) {
            if (game1.currentPlayer < vm.getAllMultiplayerOrderByScore().value!!.lastIndex) {
                game1.currentPlayer++
                vm.setGame(game1)
            } else {
                game1.round++
                game1.currentPlayer = 0
                vm.setGame(game1)
            }
            if (game1.round < 3) {
                viewMultiplayerGameReadyActivity(game1)
            } else {
                //binding.tvCurrentNamePlayer.text = playerFirst.name

                binding.tvCurrentNamePlayer.text = vm.playersOrderByScore.value?.get(0)?.name ?: ""
                binding.tvPlayerSituation.text = getString(R.string.won_the_game)
                binding.btnNext.text = getString(R.string.finalize)
                binding.btnNext.setOnClickListener { viewNewMultiplayerGameActivity() }
            }
        }
    }

    private fun viewNewMultiplayerGameActivity() {
        val intent = Intent(this, NewMultiplayerGameActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun viewMultiplayerGameReadyActivity(game: GameMultiplayer) {
        val intent = Intent(this, MultiplayerGameReadyActivity::class.java)
        intent.putExtra("Game",game)
        startActivity(intent)
        finish()
    }

    private fun addPointToThePlayer(currentPlayer: Multiplayer) {
        vm.addPointToThePlayer(currentPlayer)
        vm.setAllMultiplayerOrderByScore()
        vm.setCurrentPlayer()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

}