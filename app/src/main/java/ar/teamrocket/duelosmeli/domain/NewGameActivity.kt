package ar.teamrocket.duelosmeli.domain

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.data.database.Player
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.databinding.ActivityNewGameBinding
import ar.teamrocket.duelosmeli.ui.HomeActivity

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        injectDependencies()
        setContentView(binding.root)


        //Obtengo todos los jugadores guardados
        val allPlayers = playerDao.getAll()
        val allPlayersOrderedByName = playerDao.getAllOrderByName()

        val newPlayer = Player("",0) // como el id es autogenerado se le pone 0 y room ya sabe que id poner

        binding.btnStartGame.setOnClickListener {
            newPlayer.name = binding.etPlayerName.text.toString().replace(" ", "")

            var playerNameAlreadyExists = false
            for (player in allPlayers){
                if (player.name == newPlayer.name){
                    playerNameAlreadyExists = true
                    break
                }
            }
            when {
                playerNameAlreadyExists -> {
                    Toast.makeText(this, R.string.player_name_already_exists, Toast.LENGTH_LONG)
                        .show()
                }
                newPlayer.name == "" -> {
                    Toast.makeText(this, R.string.put_your_name, Toast.LENGTH_LONG).show()}
                else -> {
                    playerDao.insertPlayer(newPlayer) //Guardo el nuevo jugador
                    val idLastPlayer = allPlayers.size+1 //Calculamos cual es el ID que se autogeneró
                    val player = playerDao.getById(idLastPlayer.toLong()) // obtengo el nuevo jugador desde la DB.
                    viewGame(player)
                }
            }
        }

        binding.rvSelectPlayer.layoutManager = LinearLayoutManager(this)
        binding.rvSelectPlayer.adapter = PlayersAdapter(allPlayersOrderedByName)

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