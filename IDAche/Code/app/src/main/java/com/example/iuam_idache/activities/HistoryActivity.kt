package com.example.iuam_idache.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R
import com.example.iuam_idache.adapters.SymptomSelectorAdapter
import com.example.iuam_idache.adapters.WPDayPickerAdapter
import com.example.iuam_idache.apiREST.classes.ClientRestAPI
import com.example.iuam_idache.apiREST.interfaces.getMyEventsCallback
import com.example.iuam_idache.apiREST.models.EventsAche
import com.super_rabbit.wheel_picker.OnValueChangeListener
import com.super_rabbit.wheel_picker.WheelPicker

// TODO -> bug when extremity item are selected
class HistoryActivity : AppCompatActivity() {

    private lateinit var eventsDisplay : List<EventsAche>
    private lateinit var myClientRestAPI : ClientRestAPI

    //-------------- Shared preferences
    private lateinit var sharedPreferences: SharedPreferences
    private val userInfoKey : String = "user_information"

    //-------------- Buttons
    private lateinit var searchButton : ImageButton
    private lateinit var backButton : ImageButton

    //-------------- TextViews
    private lateinit var locationTextView : TextView
    private lateinit var meteoStateTextView : TextView
    private lateinit var temperatureTextView : TextView
    private lateinit var humidityTextView : TextView
    private lateinit var pressureTextView : TextView
    private lateinit var windSpeedTextView : TextView
    private lateinit var hearthBeatTextView: TextView
    private lateinit var hearthBeatMinTextView: TextView
    private lateinit var hearthBeatAverageTextView: TextView
    private lateinit var hearthBeatMaxTextView: TextView
    private lateinit var hourTextView: TextView

    //-------------- Lists
    private val hearthBeatList : MutableList<String> = mutableListOf()
    private val hearthBeatMinList : MutableList<String> = mutableListOf()
    private val hearthBeatMaxList : MutableList<String> = mutableListOf()
    private val hearthBeatAverageList : MutableList<String> = mutableListOf()
    private val windSpeedList : MutableList<String> = mutableListOf()
    private val pressureList : MutableList<String> = mutableListOf()
    private val humidityList : MutableList<String> = mutableListOf()
    private val temperatureList : MutableList<String> = mutableListOf()
    private val meteoStateList : MutableList<String> = mutableListOf()
    private val locationList : MutableList<String> = mutableListOf()



    //-------------- ImageViews
    private lateinit var meteoStateImageView : ImageView

    //-------------- DatePicker
    private lateinit var datePicker : WheelPicker
    private val nbDataToDisplay : Int = 5

    // TODO -> Change the way to manage "Nothing to show"
    private val dates : MutableList<String> = mutableListOf(
        "Nothing       "
    )

    //-------------- Adapters
    private lateinit var datePickerAdapter: WPDayPickerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //------------------------------ TextViews --------------------------------
        // Location text view
        locationTextView = findViewById(R.id.activity_main_meteo_textView_location)

        // Meteo State text view
        meteoStateTextView = findViewById(R.id.activity_main_meteo_textView_meteoState)

        // Temperature text view
        temperatureTextView = findViewById(R.id.activity_main_meteo_textView_temperature)

        // Humidity text view
        humidityTextView = findViewById(R.id.activity_main_meteo_textView_humidity)

        // Pressure text view
        pressureTextView = findViewById(R.id.activity_main_meteo_textView_pressure)

        // Wind speed text view
        windSpeedTextView = findViewById(R.id.activity_main_meteo_textView_windSpeed)

        // Hearth beat text view
        hearthBeatTextView = findViewById(R.id.activity_main_medical_textView_hearthBeat)

        // Hearth beat min text view
        hearthBeatMinTextView = findViewById(R.id.activity_main_medical_textView_hearthBeat_min)

        // Hearth beat average text view
        hearthBeatAverageTextView = findViewById(R.id.activity_main_medical_textView_hearthBeat_average)

        // Hearth beat max text view
        hearthBeatMaxTextView = findViewById(R.id.activity_main_medical_textView_hearthBeat_max)

        // Hour text view
        hourTextView = findViewById(R.id.activity_history_hour_textView)

        //------------------------------ ImageViews --------------------------------
        // Meteo state image
        meteoStateImageView = findViewById(R.id.activity_main_meteo_imageView_meteoState)

        //--------------------------------- Shared Preferences -------------------------------------

        // Get shared preferences
        sharedPreferences = getSharedPreferences(userInfoKey, Context.MODE_PRIVATE)

        // Get the user ID from shared preferences
        val userId = 3
        //val userId : Long = sharedPreferences.getLong(ProfilActivity.userIDKey, -1) // TODO -> Decomment to get the real ID

        //------------------------------------- Client API -----------------------------------------

        // Get the client request API
        myClientRestAPI = ClientRestAPI()

        // Get data from server
        myClientRestAPI.getMyEvents(userId.toInt(), object : getMyEventsCallback {
            override fun onSuccess(myEvents: List<EventsAche?>?) {

                // Display a msg to inform the user
                Toast.makeText(this@HistoryActivity, "events successfully obtained ! (userID = $userId)", Toast.LENGTH_SHORT).show()

                // Log the event success
                Log.v(
                    "TAG",
                    "getMyEvents -> Sucess (" + myEvents!!.size + ") ! : " + myEvents.toString()
                )

                //
                if (myEvents.isNotEmpty()){
                    // remove old dates
                    dates.clear()
                    // take real date events
                    myEvents.forEach {
                        dates.add(it?.event_timestamp.toString().substring(0,10) + "           " + it?.event_timestamp.toString().substring(11,19))
                        hearthBeatAverageList.add(it?.event_HR_ave.toString())
                        hearthBeatMaxList.add(it?.event_HR_max.toString())
                        hearthBeatMinList.add(it?.event_HR_min.toString())
                        pressureList.add(it?.event_pressure.toString())
                        humidityList.add(it?.event_humidity.toString())
                        temperatureList.add(it?.event_temp.toString())
                        meteoStateList.add(it?.event_code_weather.toString())

                    }
                }
                else {
                    // TODO -> Error in visualization : "Nothing" shows up only when scrolling
                    dates.clear()
                    dates.add("Nothing        ")
                    datePicker.reset()
                }

                // update list
                updateList()

                eventsDisplay = myEvents as List<EventsAche>;

            }

            override fun onFailure() {
                // Display a msg to inform the user
                Toast.makeText(this@HistoryActivity, "error : events could not be obtained...", Toast.LENGTH_SHORT).show()

                // Log the error
                Log.v("TAG", "getMyEvents -> Failed ! ")
            }
        })



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

        //----------------------------- Number picker -----------------------------
        updateList()

        // OnValueChangeListener
        datePicker.setOnValueChangeListener(object : OnValueChangeListener {
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                // TODO -> display informations in the recap views

                val position = datePickerAdapter.getPosition(newVal)

                // HearthBeat values
                hearthBeatTextView.text = hearthBeatAverageList[position]
                hearthBeatAverageTextView.text = hearthBeatAverageList[position]
                hearthBeatMinTextView.text = hearthBeatMinList[position]
                hearthBeatMaxTextView.text = hearthBeatMaxList[position]

                // Meteo values
                pressureTextView.text = pressureList[position]
                humidityTextView.text = humidityList[position]
                temperatureTextView.text = temperatureList[position]
                meteoStateTextView.text = meteoStateList[position]

                // Hours values
                hourTextView.text = dates[position].substring(11+10,11+18)


            }
        })
    }

    private fun updateList() {
        datePicker = findViewById(R.id.activity_history_datePicker)
        // Date picker
        // Set rounded wrap enable
        datePicker.setSelectorRoundedWrapPreferred(false)
        // Set wheel item count
        datePicker.setWheelItemCount(nbDataToDisplay)
        // Set wheel max index
        datePicker.setMax(dates.size - 1)
        // Set wheel min index
        datePicker.setMin(0)
        // Set selected text color
        datePicker.setSelectedTextColor(R.color.colorGreenMedium)
        // Set unselected text color
        datePicker.setUnselectedTextColor(R.color.colorGreenPrimaryDark)
        // Get the adapter
        datePickerAdapter = WPDayPickerAdapter(dates.toTypedArray(), nbDataToDisplay)
        // Set the adapter
        datePicker.setAdapter(datePickerAdapter)

    }

}
