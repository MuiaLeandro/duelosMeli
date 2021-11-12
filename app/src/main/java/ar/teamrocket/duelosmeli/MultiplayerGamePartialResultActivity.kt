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

class MultiplayerGamePartialResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerGamePartialResultBinding
    private val vm: MultiplayerGamePartialResultActivityViewModel by viewModels()


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
        vm.setAddPoint(addPoint)

        val adapter = MultiplayerScoreAdapter(playersOrderByScore)
        binding.rvScoreTableMultiplayer.layoutManager = LinearLayoutManager(this)
        binding.rvScoreTableMultiplayer.adapter = adapter

        setListeners(/*game, players.lastIndex,playersOrderByScore*/)
        setObservers(adapter)

    }

    private fun setObservers(adapter: MultiplayerScoreAdapter) {
        vm.playersOrderByScore.observe(this, {
            if (it != null) {
                adapter.setListData(it)
//                players = vm.getAllMultiplayer().value!!
//                currentPlayer = players[game.currentPlayer]


                binding.tvCurrentNamePlayer.text = vm.currentPlayer.value?.name?:""
            }
        })

        if (vm.addPoint.value == true) {
            if (vm.currentPlayer.value != null) {
                addPointToThePlayer(vm.currentPlayer.value!!)
            }
        }


    }

    private fun setListeners(/*game: GameMultiplayer, lastIndex: Int, playerFirst: List<Multiplayer>*/) {
        vm.setAllMultiplayerOrderByScore()
        vm.setListMultiplayers()
        vm.setCurrentPlayer()
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
                //game.currentPlayer++
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