package ar.teamrocket.duelosmeli.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ar.teamrocket.duelosmeli.databinding.ActivityMainMenuBinding
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.views.NewGameActivity
import ar.teamrocket.duelosmeli.ui.multiplayerActivities.view.NewMultiplayerGameActivity
import ar.teamrocket.duelosmeli.ui.userProfile.UserProfileActivity

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSinglePlayer.setOnClickListener { viewNewGame() }
        binding.btnMultiPlayer.setOnClickListener { viewNewMultiplayerGame() }
        binding.btnUserProfile.setOnClickListener{ viewUserProfile() }
        binding.btnSetup.setOnClickListener{ viewSetup() }
        binding.btnAbout.setOnClickListener {viewAboutUs() }

    }

    private fun viewNewGame() {
        val intent = Intent(this, NewGameActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun viewNewMultiplayerGame() {
        val intent = Intent(this, NewMultiplayerGameActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun viewUserProfile() {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun viewSetup() {

        Toast.makeText(this, "Por ahora no podés configurar nada", Toast.LENGTH_LONG).show()

        // Agregar SetupActivity
        //val intent = Intent(this, SetupActivity::class.java)
        //startActivity(intent)
        //finish()
    }

    private fun viewAboutUs() {

        Toast.makeText(this, "Todavía no podemos presentarnos", Toast.LENGTH_LONG).show()

        // Agregar AboutUsActivity
        //val intent = Intent(this, AboutUsActivity::class.java)
        //startActivity(intent)
        //finish()
    }

}