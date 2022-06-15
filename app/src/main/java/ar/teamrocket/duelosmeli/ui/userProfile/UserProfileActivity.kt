package ar.teamrocket.duelosmeli.ui.userProfile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.teamrocket.duelosmeli.databinding.ActivityUserProfileBinding
import ar.teamrocket.duelosmeli.ui.HomeActivity
import ar.teamrocket.duelosmeli.ui.MainMenuActivity
import ar.teamrocket.duelosmeli.ui.userProfileEdit.EditUserProfile
import com.facebook.drawee.backends.pipeline.Fresco

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivEditProfile.setOnClickListener {
            val intent = Intent(this, EditUserProfile::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}