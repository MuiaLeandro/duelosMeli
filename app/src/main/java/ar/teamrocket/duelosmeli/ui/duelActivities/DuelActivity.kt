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

class DuelActivity : AppCompatActivity() {
    lateinit var binding: ActivityDuelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gson = Gson()

        //TODO: Hacer logica para leer los productos de la activity anterior y presentarlos en pantalla
        val itemsString = intent.getStringExtra("ITEMS")
        val itemsType = object : TypeToken<MutableList<ItemDuel>>() {}.type
        val items: MutableList<ItemDuel> = gson.fromJson(itemsString, itemsType)
        Log.d("ITEMS",items.toString())

        loadUI(items[0])

        binding.iHeader.ivButtonBack.setOnClickListener{ onBackPressed() }
    }

    private fun loadUI(item: ItemDuel) {
        binding.tvProductName.text = item.title
        Picasso
            .get()
            .load(item.image)
            .noFade()
            .error(R.drawable.no_image)
            .into(binding.ivProductPicture, object : Callback {
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
                binding.btnOption1.text = getString(R.string.money_sign).plus(item.price)
                binding.btnOption2.text = getString(R.string.money_sign).plus(item.fakePrice[0])
                binding.btnOption3.text = getString(R.string.money_sign).plus(item.fakePrice[1])
            }
            2 -> {
                binding.btnOption2.text = getString(R.string.money_sign).plus(item.price)
                binding.btnOption1.text = getString(R.string.money_sign).plus(item.fakePrice[0])
                binding.btnOption3.text = getString(R.string.money_sign).plus(item.fakePrice[1])
            }
            3 -> {
                binding.btnOption3.text = getString(R.string.money_sign).plus(item.price)
                binding.btnOption1.text = getString(R.string.money_sign).plus(item.fakePrice[0])
                binding.btnOption2.text = getString(R.string.money_sign).plus(item.fakePrice[1])
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