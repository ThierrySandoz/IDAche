package com.example.iuam_idache.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R
import com.example.iuam_idache.classes.HeadachePages
import com.example.iuam_idache.fragments.SelectSymptomsFragment

class SelectSymptomActivity : AppCompatActivity() {

    // ImageButtons
    private lateinit var closeButton : ImageButton
    private lateinit var nextButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headache_select_symptoms)

        //------------------------------------- Image buttons --------------------------------------
        //---- Close button
        closeButton = findViewById(R.id.selectSymptom_toolbar_close_imageButton)
        // On button click
        closeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //---- Next button
        nextButton = findViewById(R.id.selectSymptom_toolbar_next_imageButton)
        // On button click
        nextButton.setOnClickListener {
            val intent = Intent(this, HeadacheActivity::class.java)
            startActivity(intent)
        }
    }
}