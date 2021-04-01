package com.example.iuam_idache.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R
import com.example.iuam_idache.adapters.WPDayPickerAdapter
import com.super_rabbit.wheel_picker.OnValueChangeListener
import com.super_rabbit.wheel_picker.WheelPicker

class HistoryActivity : AppCompatActivity() {

    //-------------- Buttons
    private lateinit var searchButton : ImageButton
    private lateinit var backButton : ImageButton

    //-------------- DatePicker
    private lateinit var datePicker : WheelPicker
    private val nbDataToDisplay : Int = 5
    private val dates : Array<String> = arrayOf(
        "01/01/2021",
        "02/01/2021",
        "03/01/2021",
        "04/01/2021",
        "05/01/2021",
        "06/01/2021",
        "07/01/2021",
        "08/01/2021",
        "09/01/2021",
        "10/01/2021",
        "11/01/2021",
        "12/01/2021",
        "13/01/2021",
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
        }

        //----------------------------- Number picker -----------------------------
        datePicker = findViewById(R.id.activity_history_datePicker)
        // Date picker
        // Set rounded wrap enable
        datePicker.setSelectorRoundedWrapPreferred(false)
        // Set wheel item count
        datePicker.setWheelItemCount(nbDataToDisplay)
        // Set wheel max index
        datePicker.setMax(dates.size-1)
        // Set wheel min index
        datePicker.setMin(0)
        // Set selected text color
        datePicker.setSelectedTextColor(R.color.colorGreenLight)
        // Set unselected text color
        datePicker.setUnselectedTextColor(R.color.colorGreenPrimaryDark)
        // Set user defined adapter
        datePicker.setAdapter(WPDayPickerAdapter(dates, nbDataToDisplay))

        // OnValueChangeListener
        datePicker.setOnValueChangeListener(object : OnValueChangeListener {
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                // TODO -> display informations in the recap views
            }
        })


    }

}
