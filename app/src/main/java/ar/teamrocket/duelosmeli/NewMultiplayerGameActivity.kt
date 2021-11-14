package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.databinding.ActivityNewMultiplayerGameBinding
import ar.teamrocket.duelosmeli.domain.IPlayersTeamsAdapter
import ar.teamrocket.duelosmeli.domain.PlayersTeamsAdapter
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer
import ar.teamrocket.duelosmeli.ui.viewmodels.NewMultiplayerGameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewMultiplayerGameActivity : AppCompatActivity(), IPlayersTeamsAdapter {
    private lateinit var binding: ActivityNewMultiplayerGameBinding
    private val vm: NewMultiplayerGameViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMultiplayerGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var players = emptyList<Multiplayer>()
        vm.getListMultiplayersLiveData().value.also {
            if (it != null) {
                players = it
            }
        }

        val adapter = PlayersTeamsAdapter(players, this)
        binding.rvPlayers.layoutManager = LinearLayoutManager(this)
        binding.rvPlayers.adapter = adapter

        setListeners()
        setObservers(adapter)

    }

    fun setListeners(){
        vm.setListMultiplayers()
        vm.setListMultiplayersId()
        binding.btnAddPlayer.setOnClickListener {
            addPlayer()
        }
        binding.btnDelete.setOnClickListener {
            deleteAllMultiplayer()
        }
        binding.btnNext.setOnClickListener {
            viewMultiplayerGameReadyActivity()
        }
    }

    fun setObservers(adapter: PlayersTeamsAdapter) {
        vm.team.observe(this,  {
            if (it != null) {
                adapter.setListData(it)
                //binding.tvNewMultiplayerGame.text = it[it.lastIndex].name
            }
        })
    }

    private fun addPlayer() {

        val newPlayer = Multiplayer("",0)
        newPlayer.name = binding.etPlayerName.text.toString()//.replace(" ", "")

        if (newPlayer.name.isNotEmpty()){
            vm.insertMultiplayer(newPlayer)
            binding.etPlayerName.text.clear()
        }
    }

    private fun deleteAllMultiplayer() {
        if (vm.team.value != null ){
            vm.deleteAllMultiplayer(vm.team.value!!)
        }
    }

    private fun viewMultiplayerGameReadyActivity() {
        if (vm.allPlayersId.value != null) {
            val playersId: LongArray = vm.allPlayersId.value!!.toLongArray()
            val newGame = GameMultiplayer()

            val intent = Intent(this, MultiplayerGameReadyActivity::class.java)
            intent.putExtra("Game", newGame)
            //intent.putExtra("Players", playersId)
            startActivity(intent)
            finish()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClicked(player: Multiplayer) {
        vm.deleteMultiplayer(player)
    }

}