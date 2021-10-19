package ar.teamrocket.duelosmeli.domain

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.activity.viewModels
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.room.Room
import ar.teamrocket.duelosmeli.ui.HomeActivity
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.repository.impl.MeliRepositoryImpl
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.databinding.ActivityGameBinding
import ar.teamrocket.duelosmeli.domain.impl.GameFunctionsImpl
import ar.teamrocket.duelosmeli.ui.viewmodels.GameViewModel
import com.squareup.picasso.Picasso


class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding
    private var meliRepository: MeliRepository = MeliRepositoryImpl()
    private var gameFunctions: GameFunctions = GameFunctionsImpl()
    val vm: GameViewModel by viewModels()
    var correctProcePosition: Int = 0

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
        playGame(game)
        //setListeners()
        setObservers(game)
    }

    fun setListeners(){
        TODO()
    }

    fun setObservers(game: Game){
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
            correctProcePosition = it
        })
        vm.itemPriceString.observe(this, { price ->
            //val correctPricePosition = (1..3).random()
            when (correctProcePosition) {
                1 -> {
                    binding.btnOption1.text = price
                    vm.fakePrice1.observe(this, {
                        binding.btnOption2.text = it
                    })
                    vm.fakePrice2.observe(this, {
                        binding.btnOption3.text = it
                    })
                }
                2 -> {
                    binding.btnOption2.text = price
                    vm.fakePrice1.observe(this, {
                        binding.btnOption1.text = it
                    })
                    vm.fakePrice2.observe(this, {
                        binding.btnOption3.text = it
                    })
                }
                3 -> {
                    binding.btnOption3.text = price
                    vm.fakePrice1.observe(this, {
                        binding.btnOption1.text = it
                    })
                    vm.fakePrice2.observe(this, {
                        binding.btnOption2.text = it
                    })
                }
                else -> println("Out of bounds")
            }
            successChecker(correctProcePosition, game)
        })
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


    private fun playGame(game: Game): Game {
        //var actualGame = game
        //if (actualGame.state) actualGame = searchInfo(game)
        if (game.state) vm.findCategories(game, this, binding.root)
        return game
    }

    private fun searchInfo(game: Game): Game {
        if (game.state) vm.findCategories(game, this, binding.root)
        return game
    }

    /*private fun searchCategories(game: Game) {
        meliRepository.searchCategories(game, {
            val categories = it
            val categoryId = categories[(categories.indices).random()].id
            searchItemFromCategory(categoryId, game)
        }, {
            Toast.makeText(this, it,Toast.LENGTH_LONG).show()
        }, {
            Snackbar.make(binding.root, R.string.no_internet, Snackbar.LENGTH_LONG).show()
            Log.e("Main", "Falló al obtener las categorias", it)
        })
    }*/

    /*private fun searchItemFromCategory(id: String, currentGame: Game) {
        var actualGame = currentGame
        meliRepository.searchItemFromCategory(id, currentGame, {
            apply {
                val itemsList: MutableList<Article> = mutableListOf()
                itemsList.addAll(it.results)
                val item = itemsList[(itemsList.indices).random()]
                binding.tvProductName.text = item.title

                val price = numberRounder(item.price)

                val randomNumber1to3 = (1..3).random()
                when (randomNumber1to3) {
                    1 -> binding.btnOption1.text = price
                    2 -> binding.btnOption2.text = price
                    3 -> binding.btnOption3.text = price
                    else -> println("Out of bounds")
                }
                searchItem(item.id)
                randomOptionsCalculator(item, randomNumber1to3)
                actualGame = successChecker(randomNumber1to3, actualGame)
            }
        }, {
            Toast.makeText(this, it,Toast.LENGTH_LONG).show()
        }, {
            Snackbar.make(binding.root, R.string.no_internet, Snackbar.LENGTH_LONG).show()
            Log.e("Main", "Falló al obtener los articulos de la categoría", it)
        })
    }*/

    private fun successChecker(correctOption: Int, game: Game) { // YA ESTARÍA **************************
        timer(game, correctOption)
    }

    /*private fun searchItem(id: String) {
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
    }*/

    private fun timer(game: Game, correctOption: Int) {
        var actualGame = game

        class Timer(millisInFuture: Long, countDownInterval: Long) :
            CountDownTimer(millisInFuture, countDownInterval) {
            override fun onFinish() {
                when (correctOption) {
                    1 -> oneCorrect()
                    2 -> twoCorrect()
                    else -> threeCorrect()
                }
                game.errorsCounter(actualGame); timerFunctions(actualGame)
            }
            override fun onTick(millisUntilFinished: Long) {
                //se muestra el conteo en textview
                binding.tvTime.text = (millisUntilFinished / 1000).toString()
            }
        }

        val timer = Timer(21000, 1000)
        timer.start()
        when (correctOption) {
            1 -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,true); oneCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); oneCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); oneCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
            }
            2 -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); twoCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,true); twoCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); twoCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
            }
            else -> {
                binding.btnOption1.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); threeCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption2.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,false); threeCorrect(); game.errorsCounter(actualGame); timerFunctions(actualGame)}
                binding.btnOption3.setOnClickListener { timer.cancel(); gameFunctions.optionsSounds(this,true); threeCorrect(); game.pointsCounter(actualGame); timerFunctions(actualGame)}
            }
        }
    }


    private fun timerFunctions(game: Game){
        var actualGame = game
        binding.btnOption1.isClickable = false; binding.btnOption2.isClickable = false; binding.btnOption3.isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({ colorResetter() },1000)
        Handler(Looper.getMainLooper()).postDelayed({ actualGame = continuePlayChecker(actualGame) },1500)

        gameFunctions.mistakeCounterUpdater(game, binding.ivLifeThree, binding.ivLifeTwo, binding.ivLifeOne)
    }

    private fun continuePlayChecker(game: Game): Game {

        if (game.state && game.errors < 3) searchInfo(game)
        if (game.errors == 3) {
            game.state = false

            val database = Room.databaseBuilder(
                applicationContext,
                DuelosMeliDb::class.java,
                "duelosmeli-db"
            ).allowMainThreadQueries().build()
            val playerDao = database.playerDao()

            //actualizar el jugador:
            var player = playerDao.getById(game.playerId)
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

    /*private fun numberRounder(numberDouble: Double): String {
        val numberFormatter: NumberFormat = NumberFormat.getNumberInstance(Locale.GERMAN)
        numberFormatter.roundingMode = RoundingMode.FLOOR
        return numberFormatter.format(numberDouble.toInt())
    }*/

    /*private fun randomOptionsCalculator(item: Article, correctOptionPosition: Int) {
        val randomPrice1 = randomPriceCalculator(item)
        var randomPrice2 = randomPriceCalculator(item)

        while (randomPrice1.equals(randomPrice2)) {
            randomPrice2 = randomPriceCalculator(item)
        }
        randomOptionsPosition(correctOptionPosition, randomPrice1, randomPrice2)
    }*/

    /*private fun randomOptionsPosition(correctOptionPosition: Int,
                                      randomCalculatedPrice1: Double,
                                      randomCalculatedPrice2: Double) {
        when (correctOptionPosition) {
            1 -> {
                binding.btnOption2.text = numberRounder(randomCalculatedPrice1)
                binding.btnOption3.text = numberRounder(randomCalculatedPrice2)
            }
            2 -> {
                binding.btnOption1.text = numberRounder(randomCalculatedPrice1)
                binding.btnOption3.text = numberRounder(randomCalculatedPrice2)
            }
            3 -> {
                binding.btnOption1.text = numberRounder(randomCalculatedPrice1)
                binding.btnOption2.text = numberRounder(randomCalculatedPrice2)
            }
            else -> println("Out of bounds")
        }
    }*/

    /*private fun randomPriceCalculator(item: Article): Double {
        val realPrice = item.price
        val randomNumber = (1..8).random()
        var fakePrice = 0.0

        when (randomNumber) {
            1 -> fakePrice = realPrice.times(1.10).roundToInt().toDouble()
            2 -> fakePrice = realPrice.times(1.15).roundToInt().toDouble()
            3 -> fakePrice = realPrice.times(1.20).roundToInt().toDouble()
            4 -> fakePrice = realPrice.times(1.25).roundToInt().toDouble()
            5 -> fakePrice = realPrice.times(0.90).roundToInt().toDouble()
            6 -> fakePrice = realPrice.times(0.85).roundToInt().toDouble()
            7 -> fakePrice = realPrice.times(0.80).roundToInt().toDouble()
            8 -> fakePrice = realPrice.times(0.75).roundToInt().toDouble()
            else -> println("Out of bounds")
        }
        return fakePrice
    }*/

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