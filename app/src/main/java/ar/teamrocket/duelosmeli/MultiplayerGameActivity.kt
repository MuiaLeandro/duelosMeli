package ar.teamrocket.duelosmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.repository.impl.MeliRepositoryImpl
import ar.teamrocket.duelosmeli.databinding.ActivityMultiplayerGameBinding
import ar.teamrocket.duelosmeli.domain.model.GameMultiplayer
import ar.teamrocket.duelosmeli.ui.viewmodels.GameViewModel
import ar.teamrocket.duelosmeli.ui.viewmodels.MultiplayerGameViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.HttpException

class MultiplayerGameActivity : AppCompatActivity() {
    val start = 61000L
    var timer = start
    lateinit var countDownTimer: CountDownTimer
    private lateinit var binding: ActivityMultiplayerGameBinding
    private val vm: MultiplayerGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val game = intent.extras!!.getParcelable<GameMultiplayer>("Game")!!

        playGame(game)
        startTimer(game)
        //binding.btnGuessed.setOnClickListener { guessed(game) }
        setListeners(game)
        setObservers()
    }

    private fun setObservers() {
        vm.starGame.observe(this, {
            if (it) vm.findCategories()
        })
        vm.itemNameMutable.observe(this, {
            if (it != null){
                binding.tvProductName.text = it
            }
        })
        vm.picture.observe(this, {
            if (it != null){
                Picasso.get()
                    .load(it)
                    .placeholder(R.drawable.spinner)
                    .error(R.drawable.no_image)
                    .into(binding.ivProductPicture)
            }
        })
        vm.categoriesException.observe(this, this::handleException)
        vm.itemFromCategoryException.observe(this, this::handleException)
        vm.itemException.observe(this, this::handleException)

    }

    private fun setListeners(game:GameMultiplayer) {
        vm.setCurrentPlayer()
        vm.setListMultiplayers()
        vm.setAllMultiplayerOrderByScore()
        binding.btnGuessed.setOnClickListener { guessed(game) }
    }


    private fun startTimer(game: GameMultiplayer) {
        countDownTimer = object : CountDownTimer(timer,1000){

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
        if (vm.currentPlayer.value != null) {
            vm.addPointToThePlayer(vm.currentPlayer.value!!)
        }
        viewMultiplayerGamePartialResultActivity(game, true)
    }

    private fun playGame(game: GameMultiplayer): GameMultiplayer {
        val actualGame = game
        if (actualGame.state) {
            vm.findCategories()
        }
        return actualGame
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

    private fun handleException(exception: Throwable?) {
        if (exception is HttpException)
            when (exception.code()) {
                400 -> Toast.makeText(this, R.string.bad_request.toString(), Toast.LENGTH_LONG).show()
                404 -> Toast.makeText(this, R.string.resource_not_found.toString(), Toast.LENGTH_LONG).show()
                in 500..599 -> Toast.makeText(this, R.string.server_error.toString(), Toast.LENGTH_LONG).show()
                else -> Toast.makeText(this, R.string.unknown_error.toString(), Toast.LENGTH_LONG).show()
            }
    }


}
