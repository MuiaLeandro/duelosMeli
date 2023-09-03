package ar.teamrocket.duelosmeli.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ar.teamrocket.duelosmeli.R
import ar.teamrocket.duelosmeli.databinding.ActivityHomeBinding
import ar.teamrocket.duelosmeli.domain.GameFunctions
import ar.teamrocket.duelosmeli.domain.HomeFunctions
import ar.teamrocket.duelosmeli.domain.impl.HomeFunctionsImpl
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var homeFunctions: HomeFunctions = HomeFunctionsImpl()
    private val gameFunctions: GameFunctions by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        // Se instancia una clase que implementa una interfaz con funciones para esta Activity
        homeFunctions.showImage(this, R.drawable.animated_logo, binding.ivLogo)
        homeFunctions.showImage(this, R.drawable.duelosmeli, binding.ivTitle)
        binding.btnPlayGame.setOnClickListener { viewMainMenu() }
    }

    override fun onResume() {
        super.onResume()
        // Reproducción del sonido de apertura de la app
        gameFunctions.audioPlayer(this, R.raw.open)
    }

    private fun viewMainMenu() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}