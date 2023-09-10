package ar.teamrocket.duelosmeli.ui.singleplayerActivities.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.database.Player
import ar.teamrocket.duelosmeli.databinding.ActivityNewGameBinding
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.adapters.PlayersAdapter
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels.NewGameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding
    private val vm: NewGameViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        setObservers()
        setListener()
    }

    private fun initUI() {
        binding.iHeader.tvTitle.text = getString(R.string.player)
        vm.setupAllPlayers()
    }

    private fun setListener() {
        binding.iHeader.ivButtonBack.setOnClickListener { onBackPressed() }
        binding.btnStartGame.setOnClickListener {
            handleButtonStartGame()
        }
    }

    private fun handleButtonStartGame() {
        when {
            vm.playerAlreadyExist(binding.etPlayerName.text) -> {
                Toast.makeText(this, R.string.player_name_already_exists, Toast.LENGTH_LONG)
                    .show()
            }

            vm.playerNameIsBlank(binding.etPlayerName.text) -> {
                Toast.makeText(this, R.string.put_your_name, Toast.LENGTH_LONG).show()
            }

            else -> {
                vm.insertNewPlayer(binding.etPlayerName.text)
            }
        }
    }

    private fun setObservers() {
        vm.newPlayerInserted.observe(this) {
            it?.let {
                viewGame(it)
            }
        }
        vm.allPlayersByName.observe(this) {
            it?.let {
                binding.rvSelectPlayer.layoutManager = LinearLayoutManager(this)
                binding.rvSelectPlayer.adapter = PlayersAdapter(it)
            }
        }
    }

    private fun viewGame(player: Player) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("Id", player.id)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}