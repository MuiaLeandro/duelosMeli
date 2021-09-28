package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ar.teamrocket.duelosmeli.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.database.Player
import ar.teamrocket.duelosmeli.databinding.ActivityNewGameBinding

class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Obtengo instancia de la base de datos
        val database = Room.databaseBuilder(
            applicationContext,
            DuelosMeliDb::class.java,
            "duelosmeli-db"
        ).allowMainThreadQueries().build()

        //Obtengo DAO
        val playerDao = database.playerDao()

        //ejecuto operacion
        val allPlayers = playerDao.getAll()

        val newPlayer = Player(0,"",0) // como el id es autoincremental se le pone 0 y room ya sabe que id poner

        binding.btnStartGame.setOnClickListener {
            newPlayer.name = binding.etPlayerName.text.toString()
            playerDao.insertPlayer(newPlayer)
            viewGame(newPlayer.id)
        }

        binding.rvSelectPlayer.layoutManager = LinearLayoutManager(this)
        binding.rvSelectPlayer.adapter = PlayersAdapter(allPlayers)

    }

    private fun viewGame(id:Long) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("Id",id)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

    }

}