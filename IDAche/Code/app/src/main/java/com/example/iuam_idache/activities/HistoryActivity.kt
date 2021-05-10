package com.example.iuam_idache.activities

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aigestudio.wheelpicker.WheelPicker
import com.example.iuam_idache.R
import com.example.iuam_idache.apiREST.classes.ClientRestAPI
import com.example.iuam_idache.apiREST.interfaces.getMyEventsCallback
import com.example.iuam_idache.apiREST.models.EventsAche
import com.example.iuam_idache.classes.HistoryUnit
import java.util.stream.Collectors

class HistoryActivity : AppCompatActivity() {

    private lateinit var myClientRestAPI : ClientRestAPI

    //-------------- Shared preferences
    private lateinit var sharedPreferences: SharedPreferences
    private val userInfoKey : String = "user_information"

    //-------------- Buttons
    private lateinit var searchButton : ImageButton
    private lateinit var backButton : ImageButton
    private lateinit var upButton : ImageButton
    private lateinit var downButton: ImageButton
    private lateinit var closeSearchButton : View

    //-------------- EditTexts
    private lateinit var searchEditText: EditText

    //-------------- RelativeLayouts
    private lateinit var searchRelativeLayout: RelativeLayout

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
    private val historyUnitList : MutableList<HistoryUnit> = mutableListOf(
        HistoryUnit(
            date = "Empty list",
            hour = "--:--:--",
            location = "LOCATION",
            temperature = "- -",
            humidity = "--",
            pressure = "----",
            windSpeed = "---",
            hearthBeat = "- - -",
            hearthBeatMin = "---",
            hearthBeatMax = "---",
            meteoID = -1,
            hearthBeatAverage = "---"
        )
    )
    private var historyUnitDisplayedList : MutableList<HistoryUnit> = historyUnitList

    //-------------- ImageViews
    private lateinit var meteoStateImageView : ImageView

    //-------------- Adapters
    private lateinit var dateWheelPicker: WheelPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //------------------------------ TextViews --------------------------------
        // Location text view
        locationTextView = findViewById(R.id.activity_main_meteo_textView_location)

        // Scroll the text if too long
        locationTextView.postDelayed({
            locationTextView.maxLines = 1
            locationTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
            locationTextView.marqueeRepeatLimit = 10000
            locationTextView.isSelected = true
        }, 5000)

        // Meteo State text view
        meteoStateTextView = findViewById(R.id.activity_main_meteo_textView_meteoState)

        // Scroll the text if to long
        meteoStateTextView.postDelayed({
            meteoStateTextView.maxLines = 1
            meteoStateTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
            meteoStateTextView.marqueeRepeatLimit = 10000
            meteoStateTextView.isSelected = true
        }, 5000)

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

        //------------------------------------ ImageViews ------------------------------------------
        // Meteo state image
        meteoStateImageView = findViewById(R.id.activity_main_meteo_imageView_meteoState)

        //--------------------------------- Relative layouts ---------------------------------------
        searchRelativeLayout = findViewById(R.id.history_toolbar_searchView)

        //------------------------------------ EditTexts -------------------------------------------
        searchEditText = findViewById(R.id.history_toolbar_search_inputText_editText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                dateFilter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        //------------------------------------ DatePicker ------------------------------------------
        dateWheelPicker = findViewById(R.id.activity_history_datePicker)
        updateList(historyUnitDisplayedList)

        //--------------------------------- Shared Preferences -------------------------------------
        // Get shared preferences
        sharedPreferences = getSharedPreferences(userInfoKey, Context.MODE_PRIVATE)

        // Get the user ID from shared preferences
        val userId : Long = sharedPreferences.getLong(ProfilActivity.userIDKey, -1)

        //------------------------------------- Client API -----------------------------------------
        // Get the client request API
        myClientRestAPI = ClientRestAPI()

        // Get data from server
        myClientRestAPI.getMyEvents(userId.toInt(), object : getMyEventsCallback {
            override fun onSuccess(myEvents: List<EventsAche?>?) {

                // Display a msg to inform the user
                Toast.makeText(
                    this@HistoryActivity,
                    "events successfully obtained ! (userID = $userId)",
                    Toast.LENGTH_SHORT
                ).show()

                // Log the event success
                Log.v(
                    "TAG",
                    "getMyEvents -> Sucess (" + myEvents!!.size + ") ! : " + myEvents.toString()
                )

                // remove old data
                clearAllData(historyUnitList)
                clearAllData(historyUnitDisplayedList)

                // Check if there is data in DB and fill the local list with the recovered data
                if (myEvents.isNotEmpty()) {
                    // take real date events
                    myEvents.forEach {
                        historyUnitList.add(
                            HistoryUnit(
                                date = it?.event_timestamp.toString().substring(8, 10) +
                                        "/" +
                                        it?.event_timestamp.toString().substring(5, 7) +
                                        "/" +
                                        it?.event_timestamp.toString().substring(0, 4),
                                hour = it?.event_timestamp.toString().substring(11, 19),
                                hearthBeat = it?.event_HR_ave.toString(),
                                hearthBeatAverage = it?.event_HR_ave.toString(),
                                hearthBeatMin = it?.event_HR_min.toString(),
                                hearthBeatMax = it?.event_HR_max.toString(),
                                pressure = it?.event_pressure.toString(),
                                humidity = it?.event_humidity.toString(),
                                temperature = it?.event_temp.toString(),
                                location = it?.event_localisation.toString(),
                                windSpeed = it?.event_wind_speed.toString(),
                                meteoID = it?.event_code_weather!!.toInt(),
                            )
                        )
                    }

                    historyUnitDisplayedList = historyUnitList

                    // Update the list of dates
                    updateList(historyUnitDisplayedList)

                    // Set the indicator
                    dateWheelPicker.setIndicator(true)

                    // Go to last position
                    dateWheelPicker.setSelectedItemPosition(historyUnitList.size - 1, false)

                } else {
                    // Set empty data visualization
                    setEmptyData(historyUnitList)

                    historyUnitDisplayedList = historyUnitList

                    // Remove the indicator
                    dateWheelPicker.setIndicator(false)

                    // Set the position to 0
                    dateWheelPicker.setSelectedItemPosition(0, false)
                }

                // Update the visulization
                updateVisualization(historyUnitDisplayedList)
            }

            override fun onFailure() {
                // Display a msg to inform the user
                Toast.makeText(
                    this@HistoryActivity,
                    "error : events could not be obtained...",
                    Toast.LENGTH_SHORT
                ).show()

                // Log the error
                Log.v("TAG", "getMyEvents -> Failed ! ")
            }
        })



        //------------------------------- Buttons --------------------------------
        // Back button
        backButton = findViewById(R.id.history_toolbar_back_imageButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityIfNeeded(intent, 0)

        }

        // Search button
        searchButton = findViewById(R.id.history_toolbar_search_imagebutton)
        searchButton.setOnClickListener {
            // Open the research view
            openSearchView()
        }

        // Up button
        upButton = findViewById(R.id.activity_history_buttonUp)
        upButton.setOnClickListener {
            // Increment the wheel picker value
            previousDate()
        }

        // Down button
        downButton = findViewById(R.id.activity_history_buttonDown)
        downButton.setOnClickListener {
            // Decrement the wheel picker value
            nextDate()
        }

        // Close search button
        closeSearchButton = findViewById(R.id.history_toolbar_close_search_button)
        closeSearchButton.setOnClickListener {
            // Close the research
            closeSearchView()

            // Close the keyboard
            try {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
            }

            // Recover the old data
            //historyUnitDisplayedList = historyUnitList

            // Update the visualization
            updateList(historyUnitDisplayedList)
            updateVisualization(historyUnitDisplayedList)
        }

        //----------------------------- Number picker -----------------------------
        // setOnItemSelectedListener
        dateWheelPicker.setOnItemSelectedListener { _, _, _ ->
            updateVisualization(historyUnitDisplayedList)
        }
    }

    private fun updateList(data: MutableList<HistoryUnit>) {
        // Set the data
        dateWheelPicker.data = data.stream().map(HistoryUnit::date).collect(Collectors.toList())
    }

    private fun updateVisualization(data: MutableList<HistoryUnit>) {

        // Get the current item to update the fields
        val position = dateWheelPicker.currentItemPosition

        // HearthBeat values
        if (data[position].hearthBeat == "-1") {
            hearthBeatTextView.text = "- - -"
            hearthBeatAverageTextView.text = "---"
        }
        else {
            hearthBeatTextView.text = data[position].hearthBeat
            hearthBeatAverageTextView.text = data[position].hearthBeatAverage
        }

        if (data[position].hearthBeatMin == "-1") {
            hearthBeatMinTextView.text = "---"
        }
        else {
            hearthBeatMinTextView.text = data[position].hearthBeatMin
        }

        if (data[position].hearthBeatMax == "-1") {
            hearthBeatMaxTextView.text = "---"
        }
        else {
            hearthBeatMaxTextView.text = data[position].hearthBeatMax
        }

        // Meteo values
        if (data[position].location == "" || data[position].location == "None")
            locationTextView.text = getString(R.string.Unknown)
        else
            locationTextView.text = data[position].location

        pressureTextView.text = data[position].pressure
        humidityTextView.text = data[position].humidity
        temperatureTextView.text = data[position].temperature
        val meteoID = MainActivity.weatherIconsList.findLast { weatherIcons -> weatherIcons.iconOpenWeatherID == data[position].meteoID }
        if (meteoID != null) {
            if (meteoID.description != "") {
                meteoStateTextView.text = meteoID.description
            }
            if (meteoID.iconDrawableID != -1) {
                meteoStateImageView.setImageResource(meteoID.iconDrawableID)
            }
        }
        else {
            meteoStateTextView.text = getString(R.string.unknown)
            meteoStateImageView.setImageResource(R.drawable.ic_weather_icon_cloudy)
        }
        windSpeedTextView.text = data[position].windSpeed

        // Hours values
        hourTextView.text = data[position].hour

        // Change arrow color if first or last element
        if (position == data.lastIndex) {
            downButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_gray)
        }
        else {
            downButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down)
        }

        if (position == 0) {
            upButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_gray)
        }
        else {
            upButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up)
        }
    }

    private fun nextDate() {
        if (dateWheelPicker.currentItemPosition < historyUnitDisplayedList.size-1) {
            dateWheelPicker.setSelectedItemPosition(dateWheelPicker.currentItemPosition + 1, true)

            // Wait until animation has finished
            Handler(Looper.getMainLooper()).postDelayed({
                updateVisualization(historyUnitDisplayedList)
            }, 300)
        }
    }

    private fun previousDate(){
        if (dateWheelPicker.currentItemPosition > 0) {
            dateWheelPicker.setSelectedItemPosition(dateWheelPicker.currentItemPosition - 1, true)
            // Wait until animation has finished
            Handler(Looper.getMainLooper()).postDelayed({
                updateVisualization(historyUnitDisplayedList)
            }, 300)
        }
    }

    private fun clearAllData(data: MutableList<HistoryUnit>) {
        data.clear()
    }

    private fun setEmptyData(data: MutableList<HistoryUnit>) {

        // Clear the previous data
        clearAllData(data)

        // Fill with default values
        data.add(
            HistoryUnit(
                date = "Empty list",
                hour = "--:--:--",
                location = "LOCATION",
                temperature = "- -",
                humidity = "--",
                pressure = "----",
                windSpeed = "---",
                hearthBeat = "- - -",
                hearthBeatMin = "---",
                hearthBeatMax = "---",
                meteoID = -1,
                hearthBeatAverage = "---"
            )
        )
    }

    private fun openSearchView() {
        searchEditText.setText("")
        searchRelativeLayout.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            searchRelativeLayout,
            (searchButton.right + searchButton.left) / 2,
            (searchButton.top + searchButton.bottom) / 2,
            0f, searchRelativeLayout.width.toFloat()
        )
        circularReveal.duration = 300
        circularReveal.start()
    }

    private fun closeSearchView() {
        val circularConceal = ViewAnimationUtils.createCircularReveal(
            searchRelativeLayout,
            (searchButton.right + searchButton.left) / 2,
            (searchButton.top + searchButton.bottom) / 2,
            searchRelativeLayout.width.toFloat(), 0f
        )

        circularConceal.duration = 300
        circularConceal.start()
        circularConceal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                searchRelativeLayout.visibility = View.INVISIBLE
                searchEditText.setText("")
                circularConceal.removeAllListeners()
            }
        })
    }

    private fun dateFilter(text: String) {
        val filteredList : ArrayList<HistoryUnit> = ArrayList()
        for (historyUnit : HistoryUnit in historyUnitList) {
            if (historyUnit.date.contains(text)) {
                filteredList.add(historyUnit)
            }
        }
        if (filteredList.isNotEmpty()) {
            historyUnitDisplayedList = filteredList
            dateWheelPicker.setIndicator(true)
        }
        else {
            setEmptyData(historyUnitDisplayedList)
            dateWheelPicker.setIndicator(false)
        }

        updateList(historyUnitDisplayedList)
        updateVisualization(historyUnitDisplayedList)
    }
}
