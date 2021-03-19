package com.example.iuam_idache.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R
import com.example.iuam_idache.classes.GenderType
import io.ghyeok.stickyswitch.widget.StickySwitch

class ProfilActivity : AppCompatActivity() {

    //-------------- Buttons
    private lateinit var btnNext : Button

    //-------------- StickySwitch
    private lateinit var btnGender : StickySwitch

    //-------------- EditText
    private lateinit var birthDayEditText : EditText
    private lateinit var birthMonthEditText: EditText
    private lateinit var birthYearEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var heightEditText: EditText

    //-------------- SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    private val userInfoKey : String = "user_information"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        //----------------------------- StickSwitch ------------------------------
        btnGender = findViewById(R.id.profil_stickySwitch_gender)

        //------------------------------ EditTexts -------------------------------
        birthDayEditText = findViewById(R.id.profil_edittext_birthdate_day)
        birthMonthEditText = findViewById(R.id.profil_edittext_birthdate_month)
        birthYearEditText = findViewById(R.id.profil_edittext_birthdate_year)
        weightEditText = findViewById(R.id.profil_edittext_weight)
        heightEditText = findViewById(R.id.profil_edittext_height)

        //-------------------------- SharedPreferences ---------------------------
        sharedPreferences = getSharedPreferences(userInfoKey, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // If userGender in memory -> set the stickySwitch
        if (sharedPreferences.contains(userGenderKey)) {
            when(sharedPreferences.getString(userGenderKey, GenderType.MAN)) {
                GenderType.MAN -> btnGender.setDirection(StickySwitch.Direction.LEFT, false)
                GenderType.WOMAN -> btnGender.setDirection(StickySwitch.Direction.RIGHT, false)
            }
        }

        // If birthday in memory -> Display it
        if (sharedPreferences.contains(userBirthDayKey)) {
            birthDayEditText.setText(sharedPreferences.getString(userBirthDayKey, "01"))
        }

        // If birthmonth in memory -> Display it
        if (sharedPreferences.contains(userBirthMonthKey)) {
            birthMonthEditText.setText(sharedPreferences.getString(userBirthMonthKey, "01"))
        }

        // If birthyear in memory -> Display it
        if (sharedPreferences.contains(userBirthYearKey)) {
            birthYearEditText.setText(sharedPreferences.getString(userBirthYearKey, "1900"))
        }

        // If weight in memory -> Display it
        if (sharedPreferences.contains(userWeightKey)) {
            weightEditText.setText(sharedPreferences.getString(userWeightKey, "75"))
        }

        // If height in memory -> Display it
        if (sharedPreferences.contains(userHeightKey)) {
            heightEditText.setText(sharedPreferences.getString(userHeightKey, "178"))
        }

        //------------------------------- Buttons --------------------------------
        // Next button
        btnNext = findViewById(R.id.profil_button_next)
        btnNext.setOnClickListener {

            // TODO -> Check if the data are OK

            // Get the data
            // Gender
            when(btnGender.getDirection()) {
                StickySwitch.Direction.LEFT -> editor.putString(userGenderKey, GenderType.MAN)
                StickySwitch.Direction.RIGHT -> editor.putString(userGenderKey, GenderType.WOMAN)
                else -> throw IllegalArgumentException("Error in gender selection")
            }

            // Save the data in shared preferences
            editor.putString(userBirthDayKey, birthDayEditText.text.toString())
            editor.putString(userBirthMonthKey, birthMonthEditText.text.toString())
            editor.putString(userBirthYearKey, birthYearEditText.text.toString())
            editor.putString(userHeightKey, heightEditText.text.toString())
            editor.putString(userWeightKey, weightEditText.text.toString())
            editor.apply()

            // Go to main menu
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val userGenderKey = "userGenderKey"
        const val userBirthDayKey = "userBirthDayKey"
        const val userBirthMonthKey = "userBirthMonthKey"
        const val userBirthYearKey = "userBirthYearKey"
        const val userHeightKey = "userHeightKey"
        const val userWeightKey = "userWeightKey"
    }
}