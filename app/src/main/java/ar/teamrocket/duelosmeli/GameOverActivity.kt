package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import ar.teamrocket.duelosmeli.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.database.Player
import ar.teamrocket.duelosmeli.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBackToHome.setOnClickListener { viewNewGame() }

        //ROOM
        val database = Room.databaseBuilder(
            applicationContext,
            DuelosMeliDb::class.java,
            "duelosmeli-db"
        ).allowMainThreadQueries().build()
        val playerDao = database.playerDao()
        val allPlayers = playerDao.getAll()

        val idPlayer = intent.extras!!.getLong("IdPlayer")
        //obtener jugador
        //val num: Long = 1
        var player:List<Player> = emptyList()

        if (idPlayer > 0) {
            player = playerDao.getById(idPlayer)
        }
        //playerDao.updatePlayer(player[0])
        val pointsAchieved = intent.extras!!.getInt("Points")
        val pointsAchievedString = getString(R.string.puntosLogrados, pointsAchieved)
        val pointsHighscore = getString(R.string.puntosRecord, player[0].score)

        binding.tvScoreAchieved.text = pointsAchievedString
        binding.tvHigherScore.text = pointsHighscore
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
    }

    fun viewNewGame() {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
        finish()
    }
}