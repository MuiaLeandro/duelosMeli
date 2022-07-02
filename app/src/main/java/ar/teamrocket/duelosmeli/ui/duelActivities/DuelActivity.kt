package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gson = Gson()
        val itemsString = intent.getStringExtra("ITEMS")
        val itemsType = object : TypeToken<MutableList<ItemDuel>>() {}.type
        val items: MutableList<ItemDuel> = gson.fromJson(itemsString, itemsType)
        Log.d("ITEMS", items.toString())

        vm.initViewModel(items)

        setListeners(items)
        setObservers()
    }

    private fun setListeners(items: MutableList<ItemDuel>) {
        binding.iHeaderDuel.ivButtonBack.setOnClickListener{ onBackPressed() }
        binding.btnOption1Duel.setOnClickListener {
            pressedOption(items, 1)
        }
        binding.btnOption2Duel.setOnClickListener {
            pressedOption(items, 2)
        }
        binding.btnOption3Duel.setOnClickListener {
            pressedOption(items, 3)
        }
    }

    private fun pressedOption(items: MutableList<ItemDuel>,positionPressed:Int) {
        optionIsChosen(positionPressed, vm.itemDuel.value?.correctPosition)
        if ((vm.countItem.value ?: 0) < items.size - 1) {
            Handler(Looper.getMainLooper()).postDelayed({ hideGame() }, 2000)
        } else {
            //TODO: Terminar partida
            Handler(Looper.getMainLooper()).postDelayed({ viewFinishDuel() }, 2000)
        }
    }

    private fun viewFinishDuel() {
        Toast.makeText(this, "Tu puntuacion fue: ${vm.score.value}", Toast.LENGTH_SHORT).show()
    }

    private fun hideGame() {
        binding.clLoading.visibility = View.VISIBLE
        vm.nextItem()
    }

    private fun optionIsChosen(pressedOption: Int, correctPosition: Int?) {
        showCorrectOption(correctPosition, pressedOption)
        if (correctPosition == pressedOption) {
            vm.score.value = vm.score.value?.plus(10) ?: 10
        }
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
            gameFunctions.optionsSounds(this,false)
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
            gameFunctions.optionsSounds(this,true)
        }
    }

    private fun setObservers() {
        vm.itemDuel.observe(this){
            loadUI(it)
        }
    }

    private fun loadUI(item: ItemDuel) {
        colorReset()
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
                binding.btnOption2Duel.text = getString(R.string.money_sign).plus(item.fakePrice[0])
                binding.btnOption3Duel.text = getString(R.string.money_sign).plus(item.fakePrice[1])
            }
            2 -> {
                binding.btnOption2Duel.text = getString(R.string.money_sign).plus(item.price)
                binding.btnOption1Duel.text = getString(R.string.money_sign).plus(item.fakePrice[0])
                binding.btnOption3Duel.text = getString(R.string.money_sign).plus(item.fakePrice[1])
            }
            3 -> {
                binding.btnOption3Duel.text = getString(R.string.money_sign).plus(item.price)
                binding.btnOption1Duel.text = getString(R.string.money_sign).plus(item.fakePrice[0])
                binding.btnOption2Duel.text = getString(R.string.money_sign).plus(item.fakePrice[1])
            }
            else -> println("Out of bounds")
        }
    }

    private fun showGame() {
        binding.clLoading.visibility = View.GONE
    }

    private fun colorReset(){
        binding.btnOption1Duel.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
        binding.btnOption2Duel.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
        binding.btnOption3Duel.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
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

    override fun onBackPressed() {
        super.onBackPressed()
        //TODO: ir al duel over?
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}