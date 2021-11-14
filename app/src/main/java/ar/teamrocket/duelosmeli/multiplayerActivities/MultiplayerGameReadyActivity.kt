package ar.teamrocket.duelosmeli.multiplayerActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGameReadyBinding
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer
import ar.teamrocket.duelosmeli.ui.viewmodels.MultiplayerGameReadyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MultiplayerGameReadyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerGameReadyBinding
    private val vm: MultiplayerGameReadyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerGameReadyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // recuperamos el objeto Game
        val game = intent.extras!!.getParcelable<GameMultiplayer>("Game")!!

        vm.setListMultiplayers()
        vm.setListMultiplayersId()

        var players = emptyList<Multiplayer>()
        vm.getListMultiplayersLiveData().value.also {
            if (it != null) {
                players = it
                val currentPlayer = players[game.currentPlayer]
                binding.tvNamePlayer.text = currentPlayer.name
            }
        }

        setListeners(game)
        setObservers(game)
    }

    fun setListeners(game: GameMultiplayer){
        vm.setListMultiplayers()
        vm.setListMultiplayersId()
        binding.btnMultiplayer.setOnClickListener { viewMultiplayerGameActivity(game) }
    }

    fun setObservers(game: GameMultiplayer) {
        vm.team.observe(this,  {
            if (it != null) {
                changeTextView(game)
            }
        })
    }

    private fun changeTextView(game: GameMultiplayer) {
        binding.tvNamePlayer.text= vm.team.value!![game.currentPlayer].name
    }

    private fun viewMultiplayerGameActivity(game: GameMultiplayer) {
        val intent = Intent(this, MultiplayerGameActivity::class.java)
        intent.putExtra("Game",game)

        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, NewMultiplayerGameActivity::class.java)
        startActivity(intent)

    }

}