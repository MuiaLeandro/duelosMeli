package ar.teamrocket.duelosmeli.ui.userProfile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.teamrocket.duelosmeli.databinding.ActivityUserProfileBinding
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import ar.teamrocket.duelosmeli.ui.userProfileEdit.EditUserProfile

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoToEdit.setOnClickListener {
            val intent = Intent(this, EditUserProfile::class.java)
            startActivity(intent) }
    }
}