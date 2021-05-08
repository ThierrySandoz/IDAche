package com.example.iuam_idache.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.iuam_idache.R
import com.example.iuam_idache.apiREST.classes.ClientRestAPI
import com.example.iuam_idache.apiREST.interfaces.getLongCallback
import com.example.iuam_idache.apiREST.models.EventsAche
import com.example.iuam_idache.classes.HeadachePages
import com.example.iuam_idache.classes.NumberPickerSharedViewModel
import java.util.*


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
    private var lastPage : Int = 1
    private var actualPageType : HeadachePages = HeadachePages.PAIN_LEVEL

    //-------------- SharedPreferences
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var lastSelectedPages : String
    private val headachePagesKey : String = "headache_pages"
    private lateinit var stringTokenizer: StringTokenizer
    private lateinit var symptomList: MutableList<HeadachePages>
    private lateinit var symptomValuesList : MutableList<Int>
    private lateinit var sharedPreferencesID: SharedPreferences
    private val userInfoKey : String = "user_information"
    private var userId : Long = -1

    //-------------- Shared View model
    private lateinit var model : NumberPickerSharedViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headache)

        //-------------------------- SharedPreferences ---------------------------
        sharedPreferences = getSharedPreferences(headachePagesKey, Context.MODE_PRIVATE)

        // Get user ID
        sharedPreferencesID = getSharedPreferences(userInfoKey, Context.MODE_PRIVATE)
        userId = sharedPreferencesID.getLong(ProfilActivity.userIDKey, -1)

        // Get back the last selected values
        lastSelectedPages = sharedPreferences.getString("headachePages", "").toString()
        stringTokenizer = StringTokenizer(lastSelectedPages, ",")

        symptomList = mutableListOf()

        // Add default pages
        symptomList.add(HeadachePages.PAIN_LEVEL)
        symptomList.add(HeadachePages.HEADACHE_AREA)

        val nbTokens = stringTokenizer.countTokens()
        if (nbTokens != 0) {
            for (i in 1..nbTokens) {
                symptomList.add(HeadachePages.values()[stringTokenizer.nextToken().toInt()])
            }
        }
        lastPage = symptomList.size

        // Init all values with 0
        symptomValuesList = mutableListOf()
        for (i in 1..HeadachePages.values().size) {
            symptomValuesList.add(0)
        }

        //----------------- Shared view model
        model = ViewModelProviders.of(this).get(NumberPickerSharedViewModel::class.java)
        model.actualPage.value = symptomList[actualPage-1]

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
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityIfNeeded(intent, 0)
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
            if (actualPage == 0) {
                val intent = Intent(this, SelectSymptomActivity::class.java)
                startActivity(intent)
            }
            else {
                model.actualPage.value = symptomList[actualPage-1]
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

                // Create the event
                val myEvent = EventsAche(
                    user_id = userId,
                    event_hr_ave = MainActivity.heartRateAverageValue,
                    event_hr_max = MainActivity.heartRateMaxValue,
                    event_hr_min = MainActivity.heartRateMinValue,
                    event_acc_activity = MainActivity.activityTimeValue.toInt(),
                    event_acc_sleep = MainActivity.restTimeValue.toInt(),
                    event_localisation = MainActivity.localisationValue,
                    event_humidity = MainActivity.humidityValue.toInt(),
                    event_geoloc_lat = MainActivity.latitudeValue.toFloat(),
                    event_geoloc_lon = MainActivity.longitudeValue.toFloat(),
                    event_wind_speed = MainActivity.windSpeedValue.toInt(),
                    event_temp = MainActivity.temperatureValue.toInt(),
                    event_pressure = MainActivity.pressureValue.toInt(),
                    event_code_weather = MainActivity.meteoIdValue.toString(),
                    event_visibility = -1,
                    event_ache_locality = symptomValuesList[HeadachePages.HEADACHE_AREA.ordinal],
                    event_ache_power = symptomValuesList[HeadachePages.PAIN_LEVEL.ordinal],
                    event_ache_type = symptomValuesList[HeadachePages.HEADACHE_AREA.ordinal]
                    )

                val myClientRestAPI = ClientRestAPI()
                myClientRestAPI.addNewEvents(myEvent, object : getLongCallback {
                    override fun onSuccess(myID: Long) {
                        Log.v("TAG", "i get my id event : $myID")
                        Toast.makeText(this@HeadacheActivity, "Success", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure() {
                        Log.v("TAG", "addNewEvents -> Failed ! ")
                        Toast.makeText(this@HeadacheActivity, "Failure", Toast.LENGTH_SHORT).show()
                    }
                })

                // Go back to main menu
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivityIfNeeded(intent, 0)
            }
            // If not last page -> Change page
            else {
                // Get the selected value
                symptomValuesList[symptomList[actualPage-1].ordinal] = model.selectedItem.value!!

                // Incremente pageCounter
                actualPage++

                // If last page -> change the arrow with check logo
                if (actualPage == lastPage) {
                    nextButton.setImageResource(R.drawable.ic_check)
                }

                model.actualPage.value = symptomList[actualPage-1]

                // Change the page title
                pageTitle.text = "PAGE n°$actualPage/$lastPage"
            }
        }
    }
}