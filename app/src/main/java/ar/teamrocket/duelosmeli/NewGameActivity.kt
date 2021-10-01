package ar.teamrocket.duelosmeli

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ar.teamrocket.duelosmeli.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.database.Player
import ar.teamrocket.duelosmeli.database.PlayerDao
import ar.teamrocket.duelosmeli.databinding.ActivityNewGameBinding

class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding

    private lateinit var database: DuelosMeliDb
    private lateinit var playerDao: PlayerDao
    //private lateinit var robotAdapter: RobotAdapter

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
        //this.robotAdapter = RobotAdapter { }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        injectDependencies()
        setContentView(binding.root)

/*
        //Obtengo instancia de la base de datos
        val database = Room.databaseBuilder(
            applicationContext,
            DuelosMeliDb::class.java,
            "duelosmeli-db"
        ).allowMainThreadQueries().build()

        //Obtengo DAO
        val playerDao = database.playerDao()
*/
        //ejecuto operacion
        val allPlayers = playerDao.getAll()

        val newPlayer = Player("",0) // como el id es autoincremental se le pone 0 y room ya sabe que id poner

        binding.btnStartGame.setOnClickListener {
            newPlayer.name = binding.etPlayerName.text.toString()
            playerDao.insertPlayer(newPlayer)
            val player = playerDao.getByName(newPlayer.name) // obtengo el nuevo jugador desde la DB buscandolo por el nombre porque no tengo forma de conocer el id, se queda en 0 pero se guarda bien.
            viewGame(player)
        }

        binding.rvSelectPlayer.layoutManager = LinearLayoutManager(this)
        binding.rvSelectPlayer.adapter = PlayersAdapter(allPlayers)

    }





    private fun viewGame(player: List<Player>) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("Id",player[0].id)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

    }

}