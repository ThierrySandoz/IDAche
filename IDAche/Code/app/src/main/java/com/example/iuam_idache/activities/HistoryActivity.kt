package com.example.iuam_idache.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R
import com.example.iuam_idache.adapters.WPDayPickerAdapter
import com.example.iuam_idache.apiREST.classes.ClientRestAPI
import com.example.iuam_idache.apiREST.interfaces.getMyEventsCallback
import com.example.iuam_idache.apiREST.models.EventsAche
import com.super_rabbit.wheel_picker.OnValueChangeListener
import com.super_rabbit.wheel_picker.WheelPicker

// TODO -> bug when extremity item are selected
class HistoryActivity : AppCompatActivity() {

    private lateinit var eventsDisplay : List<EventsAche>

    //-------------- Buttons
    private lateinit var searchButton : ImageButton
    private lateinit var backButton : ImageButton

    //-------------- DatePicker
    private lateinit var datePicker : WheelPicker
    private val nbDataToDisplay : Int = 5
    private val dates : MutableList<String> = mutableListOf(
        "01/01/2021",
        "02/01/2021",
        "03/01/2021",
        "04/01/2021",
        "05/01/2021",
//        "06/01/2021",
//        "07/01/2021",
//        "08/01/2021",
//        "09/01/2021",
//        "10/01/2021",
//        "11/01/2021",
//        "12/01/2021",
//        "13/01/2021",
    )

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

            // TODO get my real ID
            // Get all my events
            val myUserID = 3

            val myClientRestAPI = ClientRestAPI()
            myClientRestAPI.getMyEvents(myUserID, object : getMyEventsCallback {
                override fun onSuccess(myEvents: List<EventsAche?>?) {
                    // TODO : add notif
                    Log.v(
                        "TAG",
                        "getMyEvents -> Sucess (" + myEvents!!.size + ") ! : " + myEvents.toString()
                    )

                    // remove old dates
                    dates.removeAll { true }
                    // take real date events
                    myEvents.forEach { dates.add(it?.event_timestamp.toString().substring(0,10)) }

                    // update list
                    updateList()
                    eventsDisplay = myEvents as List<EventsAche>;

                }

                override fun onFailure() {
                    // TODO : add notif
                    Log.v("TAG", "getMyEvents -> Failed ! ")
                }
            })
        }

        //----------------------------- Number picker -----------------------------
        updateList()

        // OnValueChangeListener
        datePicker.setOnValueChangeListener(object : OnValueChangeListener {
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                // TODO -> display informations in the recap views
                // https://stackoverflow.com/questions/33053765/how-to-make-a-wheel-picker/39662187
                Log.v("TAG", "onValueChange old : " + oldVal + "  new : " + newVal)
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
        datePicker.setSelectedTextColor(R.color.colorGreenLight)
        // Set unselected text color
        datePicker.setUnselectedTextColor(R.color.colorGreenPrimaryDark)
        // Set user defined adapter
        datePicker.setAdapter(WPDayPickerAdapter(dates.toTypedArray(), nbDataToDisplay))
    }

}
