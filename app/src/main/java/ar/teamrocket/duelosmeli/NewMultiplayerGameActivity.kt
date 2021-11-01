package ar.teamrocket.duelosmeli

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.data.database.Multiplayer
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.databinding.ActivityNewMultiplayerGameBinding
import ar.teamrocket.duelosmeli.domain.PlayersTeamsAdapter
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer

class NewMultiplayerGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewMultiplayerGameBinding

    private lateinit var database: DuelosMeliDb
    private lateinit var playerDao: PlayerDao

    companion object {
        private fun buildDatabase(context: Context): DuelosMeliDb {
            return Room.databaseBuilder(
                context,
                DuelosMeliDb::class.java,
                "duelosmeli-db"
            ).allowMainThreadQueries().build()
        }
    }


    /**
     * Manual dependency injection
     */
    private fun injectDependencies() {
        this.database = buildDatabase(this.applicationContext)
        this.playerDao = this.database.playerDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMultiplayerGameBinding.inflate(layoutInflater)
        injectDependencies()
        setContentView(binding.root)

        val newPlayer = Multiplayer("",0) // como el id es autogenerado no se le pone y room ya sabe que id poner
        val players = playerDao.getAllMultiplayer()
        initializeScores(players)

        binding.rvPlayers.layoutManager = LinearLayoutManager(this)
        binding.rvPlayers.adapter = PlayersTeamsAdapter(players)
        binding.btnAddPlayer.setOnClickListener { addPlayer(newPlayer) }
        binding.btnDelete.setOnClickListener { deleteAllPlayers(players) }
        binding.btnNext.setOnClickListener { viewMultiplayerGameReadyActivity() }
    }

    private fun deleteAllPlayers(players: List<Multiplayer>) {
        playerDao.deleteAllMultiplayers(players)
    }

    private fun initializeScores(players: List<Multiplayer>) {
        if (players.isNotEmpty()) {
            for (player in players){
                player.score = 0
            }
            playerDao.updateMultiplayers(players)
        }
    }

    private fun addPlayer(newPlayer:Multiplayer) {
        newPlayer.name = binding.etPlayerName.text.toString().replace(" ", "")
        playerDao.insertMultiplayer(newPlayer) //Guardo el nuevo jugador
        binding.etPlayerName.text.clear()
    }

    private fun viewMultiplayerGameReadyActivity() {
        val playersId = playerDao.getAllMultiplayerId().toTypedArray()
        val newGame = GameMultiplayer()

        val intent = Intent(this, MultiplayerGameReadyActivity::class.java)
        intent.putExtra("Game",newGame)
        intent.putExtra("Players",playersId)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

}