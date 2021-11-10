package ar.teamrocket.duelosmeli

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGameBinding
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGameReadyBinding
import ar.teamrocket.duelosmeli.databinding.ActivityNewMultiplayerGameBinding
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer
import ar.teamrocket.duelosmeli.ui.HomeActivity

class MultiplayerGameReadyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerGameReadyBinding
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
        binding = ActivityMultiplayerGameReadyBinding.inflate(layoutInflater)
        injectDependencies()
        setContentView(binding.root)

        // recuperamos el objeto Game
        val game = intent.extras!!.getParcelable<GameMultiplayer>("Game")!!

        //val players = playerDao.getAllMultiplayer()
//        val currentPlayer = players[game.currentPlayer]

//        binding.tvNamePlayer.text = currentPlayer.name
        binding.btnMultiplayer.setOnClickListener { viewMultiplayerGameActivity(game) }
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