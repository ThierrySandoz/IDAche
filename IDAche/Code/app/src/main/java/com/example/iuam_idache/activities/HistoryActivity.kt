package com.example.iuam_idache.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R

class HistoryActivity : AppCompatActivity() {

    //-------------- Buttons
    private lateinit var searchButton : ImageButton
    private lateinit var backButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //------------------------------- Buttons --------------------------------
        // Back button
        backButton = findViewById(R.id.history_toolbar_back_imageButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Search button
        searchButton = findViewById(R.id.history_toolbar_search_imagebutton)
        searchButton.setOnClickListener {
            // TODO -> make a research in the database
        }



    }

}