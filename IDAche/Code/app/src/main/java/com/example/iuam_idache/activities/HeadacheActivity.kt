package com.example.iuam_idache.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.iuam_idache.R
import com.example.iuam_idache.adapters.SymptomSelectorAdapter
import com.example.iuam_idache.fragments.SelectSymptomsFragment

class HeadacheActivity : AppCompatActivity() {
    //-------------- Buttons
    private lateinit var closeButton : ImageButton
    private lateinit var helpButton : ImageButton
    private lateinit var backButton : ImageButton
    private lateinit var nextButton : ImageButton

    //-------------- TextViews
    private lateinit var pageTitle : TextView

    //-------------- Variables
    private var actualPage : Int = 1
    private val lastPage : Int = 4

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headache)

        //------------------------------ TextViews -------------------------------
        //------ Page title
        pageTitle = findViewById(R.id.headache_toolbar_title_textView)
        // Set the page title with the actual page
        pageTitle.text = "PAGE n°$actualPage/$lastPage"

        //------------------------------- Buttons --------------------------------
        //------ Close button
        closeButton = findViewById(R.id.headache_toolbar_close_imageButton)

        // On button click
        closeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //------ Help button
        helpButton = findViewById(R.id.headache_toolbar_help_imageButton)

        // On button click
        helpButton.setOnClickListener {
            // TODO -> Do something when click on help
        }

        //------ Back button
        backButton = findViewById(R.id.headache_toolbar_back_imageButton)

        // If first page -> set visibility to invisible

        // On button click
        backButton.setOnClickListener {

            // If last page -> change check logo with right arrow
            if (actualPage == lastPage) {
                nextButton.setImageResource(R.drawable.ic_right_arrow)
            }

            // Decrement page counter
            actualPage--

            // If 1st page -> disable back button
            if (actualPage == 1) {
                backButton.visibility = View.INVISIBLE
            }

            // Change the page title
            pageTitle.text = "PAGE n°$actualPage/$lastPage"
        }

        //------ Next button
        nextButton = findViewById(R.id.headache_toolbar_next_imageButton)

        // On button click
        nextButton.setOnClickListener {

            // If 1st page -> activate back button
            if (actualPage == 1) {
                // Set the back button to visible
                backButton.visibility = View.VISIBLE
            }

            // If last page -> Save the data + go back to main menu
            if (actualPage == lastPage) {
                // TODO -> Save the data

                // Go back to main menu
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            // If not last page -> Change page
            else {
                // Incremente pageCounter
                actualPage++

                // If last page -> change the arrow with check logo
                if (actualPage == lastPage) {
                    nextButton.setImageResource(R.drawable.ic_check)
                }

                // Change the page title
                pageTitle.text = "PAGE n°$actualPage/$lastPage"
            }
        }


    }

}