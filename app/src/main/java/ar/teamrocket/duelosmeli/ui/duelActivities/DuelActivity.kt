package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.model.ItemDuel
import ar.teamrocket.duelosmeli.databinding.ActivityDuelBinding
import ar.teamrocket.duelosmeli.domain.GameFunctions
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DuelActivity : AppCompatActivity() {
    lateinit var binding: ActivityDuelBinding
    private val vm: DuelGameViewModel by viewModel()
    private val gameFunctions: GameFunctions by inject()
    private val start = 21000L
    var timer = start
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val gson = Gson()
        val itemsString = intent.getStringExtra("ITEMS")
        val itemsType = object : TypeToken<MutableList<ItemDuel>>() {}.type
        val items: MutableList<ItemDuel> = gson.fromJson(itemsString, itemsType)
        Log.d("ITEMS", items.toString())
        binding.clLoading.visibility = View.VISIBLE

        vm.initViewModel(items)
        
        setListeners()
        setObservers(items)
    }

    private fun setListeners() {
        binding.iHeaderDuel.ivButtonBack.setOnClickListener{ onBackPressed() }
        binding.btnOption1Duel.setOnClickListener {
            pressedOption(1)
        }
        binding.btnOption2Duel.setOnClickListener {
            pressedOption(2)
        }
        binding.btnOption3Duel.setOnClickListener {
            pressedOption(3)
        }
    }

    private fun startTimer() {
        var i = 1
        countDownTimer = object : CountDownTimer(timer, 1) {

            override fun onFinish() {
                when (vm.itemDuel.value?.correctPosition) {
                    1 -> oneCorrect()
                    2 -> twoCorrect()
                    else -> threeCorrect()
                }
                nextItemOrEndGame()
                timer = start
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onTick(millisUntilFinished: Long) {
                i++
                binding.pbDeterminateBarDuel.setProgress(
                    i * 100 / ((start.toInt() - 1500) / 1),
                    true
                )
            }
        }.start()
        binding.clLoading.visibility = View.GONE
    }

    private fun oneCorrect() {
        binding.btnOption1Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green1,
            null))
        binding.btnOption2Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red1,
            null))
        binding.btnOption3Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red1,
            null))
    }

    private fun twoCorrect() {
        binding.btnOption1Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red1,
            null))
        binding.btnOption2Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green1,
            null))
        binding.btnOption3Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red1,
            null))
    }

    private fun threeCorrect() {
        binding.btnOption1Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red1,
            null))
        binding.btnOption2Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red1,
            null))
        binding.btnOption3Duel.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green1,
            null))
    }

    private fun pressedOption(positionPressed:Int) {
        pauseTimer()
        optionIsChosen(positionPressed, vm.itemDuel.value?.correctPosition)
        nextItemOrEndGame()
    }

    private fun nextItemOrEndGame() {
        if ((vm.positionItem.value ?: 0) < vm.itemsDuel.size - 1) {
            Handler(Looper.getMainLooper()).postDelayed({ showNextItem() }, 2000)
        } else {

            Handler(Looper.getMainLooper()).postDelayed({ viewFinishDuel() }, 2000)
        }
    }

    private fun viewFinishDuel() {
        val intent = Intent(this,DuelOverActivity::class.java)
        intent.putExtra(DuelOverActivity.EXT_POINTS,vm.score.value)
        startActivity(intent)
    }

    private fun showNextItem() {
        binding.clLoading.visibility = View.VISIBLE
        vm.nextItem()
    }

    private fun optionIsChosen(pressedOption: Int, correctPosition: Int?) {
        showCorrectOption(correctPosition, pressedOption)
        if (correctPosition == pressedOption) {
            calculateScore()
        }
    }

    private fun calculateScore() {
        /*
        Como calcularemos la puntuacion:
        preguntas bien x 1.000
        segundos restantes x 10
        */

        //contesto bien asi que sumamos 1000
        vm.score.value = vm.score.value?.plus(1000) ?: 1000
        //timer son los milisegundos que le quedaban
        vm.score.value = vm.score.value?.plus(timer.toInt()/100) ?: 0
    }

    private fun showCorrectOption(correctPosition: Int?, pressedOption: Int) {
        when (correctPosition) {
            1 -> {
                binding.btnOption1Duel.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources, R.color
                            .green1, null
                    )
                )
            }
            2 -> {
                binding.btnOption2Duel.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources, R.color
                            .green1, null
                    )
                )
            }
            else -> {
                binding.btnOption3Duel.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources, R.color
                            .green1, null
                    )
                )
            }
        }
        if (correctPosition != pressedOption){
            gameFunctions.optionsSounds(this,false, R.raw.correct, R.raw.incorrect)
            when (pressedOption) {
                1 -> { binding.btnOption1Duel
                    .setBackgroundColor(ResourcesCompat.getColor(resources,
                    R.color.red1,null))
                }
                2 -> { binding.btnOption2Duel
                    .setBackgroundColor(ResourcesCompat.getColor(resources,
                    R.color.red1,null))
                }
                else -> { binding.btnOption3Duel
                    .setBackgroundColor(ResourcesCompat.getColor (resources,
                    R.color.red1,null))
                }
            }
        } else {
            gameFunctions.optionsSounds(this,true, R.raw.correct, R.raw.incorrect)
        }
    }

    private fun setObservers(items: MutableList<ItemDuel>) {
        vm.itemDuel.observe(this){
            loadUI(it)
        }
        vm.positionItem.observe(this){
            binding.tvRoundDuel.text="${it+1}/${items.size}"
        }
    }

    private fun loadUI(item: ItemDuel) {
        gameFunctions.colorFormatter(this, listOf(binding.btnOption1Duel, binding.btnOption2Duel, binding.btnOption3Duel), R.attr.colorPrimary)
        binding.tvProductNameDuel.text = item.title
        Picasso
            .get()
            .load(item.image)
            .noFade()
            .error(R.drawable.no_image)
            .into(binding.ivProductPictureDuel, object : Callback {
                override fun onSuccess() {
                    //mostrar pantalla del juego
                    Handler(Looper.getMainLooper()).postDelayed({ showGame() }, 2000)
                }
                override fun onError(e: java.lang.Exception?) {
                    //do smth when there is picture loading error
                }
            })
        when (item.correctPosition) {
            1 -> {
                binding.btnOption1Duel.text = getString(R.string.money_sign).plus(item.price)
                binding.btnOption2Duel.text = getString(R.string.money_sign).plus(item.fakePrice1)
                binding.btnOption3Duel.text = getString(R.string.money_sign).plus(item.fakePrice2)
            }
            2 -> {
                binding.btnOption2Duel.text = getString(R.string.money_sign).plus(item.price)
                binding.btnOption1Duel.text = getString(R.string.money_sign).plus(item.fakePrice1)
                binding.btnOption3Duel.text = getString(R.string.money_sign).plus(item.fakePrice2)
            }
            3 -> {
                binding.btnOption3Duel.text = getString(R.string.money_sign).plus(item.price)
                binding.btnOption1Duel.text = getString(R.string.money_sign).plus(item.fakePrice1)
                binding.btnOption2Duel.text = getString(R.string.money_sign).plus(item.fakePrice2)
            }
            else -> println("Out of bounds")
        }
    }

    private fun showGame() {
        startTimer()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //TODO: ir al duel over?
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}