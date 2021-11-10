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
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGamePartialResultBinding
import ar.teamrocket.duelosmeli.domain.MultiplayerScoreAdapter
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer

class MultiplayerGamePartialResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerGamePartialResultBinding
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
        binding = ActivityMultiplayerGamePartialResultBinding.inflate(layoutInflater)
        injectDependencies()
        setContentView(binding.root)


        val game = intent.extras!!.getParcelable<GameMultiplayer>("Game")!!
        val addPoint = intent.extras!!.getBoolean("AddPoint")
        val playersOrderByScore = playerDao.getAllMultiplayerOrderByScore()
        //val players = playerDao.getAllMultiplayer()
        //val currentPlayer = players[game.currentPlayer]

        if (addPoint) {
//            addPointToThePlayer(currentPlayer)
            binding.tvPlayerSituation.text = getString(R.string.guessed)
        } else {
            binding.tvPlayerSituation.text = getString(R.string.did_not_guessed)
        }
        //binding.tvCurrentNamePlayer.text = currentPlayer.name

        binding.rvScoreTableMultiplayer.layoutManager = LinearLayoutManager(this)
        binding.rvScoreTableMultiplayer.adapter = MultiplayerScoreAdapter(playersOrderByScore)
        //binding.btnNext.setOnClickListener { nextView(game, players.lastIndex,playersOrderByScore[0]) }
    }

    private fun nextView(game: GameMultiplayer, lastIndex: Int, playerFirst: Multiplayer) {
        if (game.currentPlayer < lastIndex){
            game.currentPlayer++
        } else {
            game.round++
            game.currentPlayer = 0
        }
        if (game.round < 3) {
            viewMultiplayerGameReadyActivity(game)
        } else {
            binding.tvCurrentNamePlayer.text = playerFirst.name
            binding.tvPlayerSituation.text = getString(R.string.won_the_game)
            binding.btnNext.text = getString(R.string.finalize)
            binding.btnNext.setOnClickListener { viewNewMultiplayerGameActivity() }
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
        currentPlayer.score++
        playerDao.updateMultiplayer(currentPlayer)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)

    }

}