package ar.teamrocket.duelosmeli.ui.singleplayerActivities.views

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import ar.teamrocket.duelosmeli.ui.HomeActivity
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.databinding.ActivityGameBinding
import ar.teamrocket.duelosmeli.domain.Game
import ar.teamrocket.duelosmeli.domain.GameFunctions
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels.GameViewModel
import com.squareup.picasso.Picasso
import retrofit2.HttpException
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject

class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding
    private val start = 21000L
    var timer = start
    private lateinit var countDownTimer: CountDownTimer
    private val gameFunctions: GameFunctions by inject()
    private val vm: GameViewModel by viewModel()
    private val playerDao : PlayerDao by inject()
    private var correctPricePosition: Int = 0
    private lateinit var fake1: String
    private lateinit var fake2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val doorbellSound = MediaPlayer.create(this, R.raw.doorbell)
        doorbellSound.start()

        val playerId = intent.extras!!.getLong("Id")
        val game = Game(playerId)

        binding.btnExitGame.setOnClickListener { viewGameOver(game) }

        gameFunctions.mistakeCounterUpdater(game, binding.ivLifeThree, binding.ivLifeTwo, binding.ivLifeOne)

        setListeners()
        setObservers(game)
    }

    private fun continueGame(){
        vm.findCategories()
    }

    private fun setListeners(){
        vm.starGame.observe(this, {
            if (it) vm.findCategories()
        })
    }

    private fun setObservers(game: Game){
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
        vm.randomNumber1to3Mutable.observe(this, {
            correctPricePosition = it
        })

        vm.itemPriceString.observe(this, { price ->
            when (correctPricePosition) {
                1 -> binding.btnOption1.text = getString(R.string.money_sign).plus(price)
                2 -> binding.btnOption2.text = getString(R.string.money_sign).plus(price)
                3 -> binding.btnOption3.text = getString(R.string.money_sign).plus(price)
                else -> println("Out of bounds")
            }
            vm.fakePrice1.observe(this, {
                fake1 = it
                when (correctPricePosition) {
                    1 -> binding.btnOption2.text = getString(R.string.money_sign).plus(fake1)
                    2 -> binding.btnOption1.text = getString(R.string.money_sign).plus(fake1)
                    3 -> binding.btnOption1.text = getString(R.string.money_sign).plus(fake1)
                    else -> println("Out of bounds")
                }
            })
            vm.fakePrice2.observe(this, {
                fake2 = it
                when (correctPricePosition) {
                    1 -> binding.btnOption3.text = getString(R.string.money_sign).plus(fake2)
                    2 -> binding.btnOption3.text = getString(R.string.money_sign).plus(fake2)
                    3 -> binding.btnOption2.text = getString(R.string.money_sign).plus(fake2)
                    else -> println("Out of bounds")
                }
            })
            successChecker(correctPricePosition, game)
        })
        vm.categoriesException.observe(this, this::handleException)
        vm.itemFromCategoryException.observe(this, this::handleException)
        vm.itemException.observe(this, this::handleException)
        // Para probar un snackbar y ver diferencia con Toast
        /*vm.itemException.observe(this, {
            Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
        })*/
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

    private fun viewGameOver(game: Game) {
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("Points",game.points)
        intent.putExtra("IdPlayer",game.playerId)

        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    
    private fun successChecker(correctOption: Int, game: Game) {
        startTimer(game, correctOption)
    }

    private fun startTimer(game: Game, correctOption: Int) {
        countDownTimer = object : CountDownTimer(timer,1000){
            //            end of timer
            override fun onFinish() {
                when (correctOption) {
                    1 -> oneCorrect()
                    2 -> twoCorrect()
                    else -> threeCorrect()
                }
                game.errorsCounter(game); timerFunctions(game)
                timer = 21000L
            }

            override fun onTick(millisUntilFinished: Long) {
                timer = millisUntilFinished
                setTextTimer()
            }

        }.start()
        optionsButtons(game, correctOption)
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

    private fun optionsButtons(game: Game, correctOption: Int){
        binding.btnOption1.setOnClickListener{
            optionIsChosen(1, correctOption, game)
        }

        binding.btnOption2.setOnClickListener{
            optionIsChosen(2, correctOption, game)
        }

        binding.btnOption3.setOnClickListener{
            optionIsChosen(3, correctOption, game)
        }
    }

    private fun optionIsChosen(pressedOption: Int, correctOption: Int, game: Game) {
        pauseTimer()
        timer = 21000L
        showCorrectOption(correctOption, pressedOption)
        if (correctOption == pressedOption) {
            game.pointsCounter(game)
        } else {
            game.errorsCounter(game)
        }
        timerFunctions(game)
    }

    private fun showCorrectOption(correctOption: Int, pressedOption: Int) {
        when (correctOption) {
            1 -> {
                binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green,null))
            }
            2 -> {
                binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green,null))
            }
            else -> {
                binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green,null))
            }
        }

        if (correctOption != pressedOption){
            gameFunctions.optionsSounds(this,false)
            when (pressedOption) {
                1 -> { binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null)) }
                2 -> { binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null)) }
                else -> { binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null)) }
            }
        } else {
            gameFunctions.optionsSounds(this,true)
        }
    }


    private fun timerFunctions(game: Game){
        var actualGame = game
        binding.btnOption1.isClickable = false; binding.btnOption2.isClickable = false; binding.btnOption3.isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({ colorResetter() },1000)
        Handler(Looper.getMainLooper()).postDelayed({ actualGame = continuePlayChecker(actualGame) },0)

        gameFunctions.mistakeCounterUpdater(game, binding.ivLifeThree, binding.ivLifeTwo, binding.ivLifeOne)
    }

    private fun continuePlayChecker(game: Game): Game {

        if (game.state && game.errors < 3) continueGame()
        if (game.errors == 3) {
            game.state = false

            //actualizar el jugador:
            val player = playerDao.getById(game.playerId)
            if (player.isNotEmpty() && game.points > player[0].score ) {
                player[0].score = game.points
                playerDao.updatePlayer(player[0])
            }
            //ir a GameOver:
            Handler(Looper.getMainLooper()).postDelayed({ viewGameOver(game) }, 2000)
        }
        return game
    }

    private fun oneCorrect() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green,null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
    }

    private fun twoCorrect() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
    }

    private fun threeCorrect() {
        binding.btnOption1.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
        binding.btnOption2.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red,null))
        binding.btnOption3.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green,null))
    }
    

    private fun colorResetter(){
        binding.btnOption1.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
        binding.btnOption2.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
        binding.btnOption3.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
    }

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }
}