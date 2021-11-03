package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.repository.impl.MeliRepositoryImpl
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGameBinding
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class MultiplayerGameActivity : AppCompatActivity() {
    val start = 61000L
    var timer = start
    lateinit var countDownTimer: CountDownTimer
    private lateinit var binding: ActivityMultiplayerGameBinding
    private var meliRepository: MeliRepository = MeliRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val game = intent.extras!!.getParcelable<GameMultiplayer>("Game")!!
        playGame(game)
        startTimer(game)
        binding.btnGuessed.setOnClickListener { guessed(game) }
    }


    private fun startTimer(game: GameMultiplayer) {
        countDownTimer = object : CountDownTimer(timer,1000){
            //            end of timer
            override fun onFinish() {
                viewMultiplayerGamePartialResultActivity(game, false)
            }

            override fun onTick(millisUntilFinished: Long) {
                timer = millisUntilFinished
                setTextTimer()
            }

        }.start()
    }
    private fun pauseTimer() {
        countDownTimer.cancel()
    }

    private fun setTextTimer() {
        val m = (timer / 1000) / 60
        val s = (timer / 1000) % 60

        val format = String.format("%02d:%02d", m, s)

        binding.tvTime.text = format
    }


    private fun guessed(game: GameMultiplayer) {
        pauseTimer()
        viewMultiplayerGamePartialResultActivity(game, true)
    }

    private fun playGame(game: GameMultiplayer): GameMultiplayer {
        var actualGame = game
        if (actualGame.state) {
            actualGame = searchInfo(game)
        }
        return actualGame
    }

    private fun searchInfo(game: GameMultiplayer): GameMultiplayer {
        if (game.state) searchCategories()
        return game
    }

    private fun searchCategories() {
        meliRepository.searchCategories({
            val categories = it
            val categoryId = categories[(categories.indices).random()].id
            searchItemFromCategory(categoryId)
        }, {
            Toast.makeText(this, it,Toast.LENGTH_LONG).show()
        }, {
            Snackbar.make(binding.root, R.string.no_internet, Snackbar.LENGTH_LONG).show()
            Log.e("Main", "Falló al obtener las categorias", it)
        })
    }

    private fun searchItemFromCategory(id: String) {
        meliRepository.searchItemFromCategory(id, {
            apply {
                val itemsList: MutableList<Article> = mutableListOf()
                itemsList.addAll(it.results)
                val item = itemsList[(itemsList.indices).random()]
                binding.tvProductName.text = item.title

                searchItem(item.id)
            }
        }, {
            Toast.makeText(this, it,Toast.LENGTH_LONG).show()
        }, {
            Snackbar.make(binding.root, R.string.no_internet, Snackbar.LENGTH_LONG).show()
            Log.e("Main", "Falló al obtener los articulos de la categoría", it)
        })
    }

    private fun searchItem(id: String) {
        meliRepository.searchItem(id, {
            apply {
                Picasso.get()
                    .load(it.pictures[0].secureUrl)
                    .placeholder(R.drawable.spinner)
                    .error(R.drawable.no_image)
                    .into(binding.ivProductPicture)
            }
        }, {
            Toast.makeText(this, it,Toast.LENGTH_LONG).show()
        }, {
            Snackbar.make(binding.root, R.string.no_internet, Snackbar.LENGTH_LONG).show()
            Log.e("Main", "Falló al obtener el artículo", it)
        })
    }

    private fun viewMultiplayerGamePartialResultActivity(game: GameMultiplayer, addPoint: Boolean) {
        val intent = Intent(this, MultiplayerGamePartialResultActivity::class.java)
        intent.putExtra("Game",game)
        intent.putExtra("AddPoint",addPoint)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)

        startActivity(intent)

    }

}
