package ar.teamrocket.duelosmeli.ui.duelActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.data.model.ItemDuel
import ar.teamrocket.duelosmeli.databinding.ActivityDuelBinding
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DuelActivity : AppCompatActivity() {
    lateinit var binding: ActivityDuelBinding
    private val vm: DuelGameViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gson = Gson()

        //TODO: Hacer logica para leer los productos de la activity anterior y presentarlos en pantalla
        val itemsString = intent.getStringExtra("ITEMS")
        val itemsType = object : TypeToken<MutableList<ItemDuel>>() {}.type
        val items: MutableList<ItemDuel> = gson.fromJson(itemsString, itemsType)
        Log.d("ITEMS", items.toString())


        vm.initViewModel(items)
        binding.btnOption1Duel.setOnClickListener {
            if ((vm.countItem.value ?: 0) < items.size-1) {
                vm.nextItem()
            } else {
                //TODO: Terminar partida
            }
        }
        binding.iHeaderDuel.ivButtonBack.setOnClickListener{ onBackPressed() }
        setObservers()
    }

    private fun setObservers() {
        vm.itemDuel.observe(this){
            loadUI(it)
        }
    }

    private fun loadUI(item: ItemDuel) {
        binding.tvProductNameDuel.text = item.title
        Picasso
            .get()
            .load(item.image)
            .noFade()
            .error(R.drawable.no_image)
            .into(binding.ivProductPictureDuel, object : Callback {
                override fun onSuccess() {
                    //mostrar pantalla del juego
                    binding.clLoading.visibility = View.GONE
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

    override fun onBackPressed() {
        super.onBackPressed()
        //TODO: ir al duel over?
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}