package com.example.iuam_idache.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    //-------------- Buttons
    private lateinit var historyButton : ImageButton
    private lateinit var profilButton : ImageButton
    private lateinit var headacheButton : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //------------------------------- Buttons --------------------------------
        // History button
        historyButton = findViewById(R.id.main_toolbar_imageButton_history)
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // Profil button
        profilButton = findViewById(R.id.main_toolbar_imageButton_profil)
        profilButton.setOnClickListener {
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
        }

        // Headache floating button
        headacheButton = findViewById(R.id.main_floatingButton_headache)
        headacheButton.setOnClickListener {
            val intent = Intent(this, HeadacheActivity::class.java)
            startActivity(intent)
        }

    }

}