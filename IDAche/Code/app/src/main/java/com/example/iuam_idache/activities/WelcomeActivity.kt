package com.example.iuam_idache.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R

class WelcomeActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var sharedPreferences: SharedPreferences
    private val userInfoKey : String = "user_information"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Switch to profile activity after 1s
        handler = Handler()
        handler.postDelayed({

            // Get shared preferences
            sharedPreferences = getSharedPreferences(userInfoKey, Context.MODE_PRIVATE)

            // If profile already exist -> go to main menu
            if (isProfileExisting()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, ProfilActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)
    }

    private fun isProfileExisting(): Boolean {
        return sharedPreferences.contains(ProfilActivity.userIDKey)
    }
}