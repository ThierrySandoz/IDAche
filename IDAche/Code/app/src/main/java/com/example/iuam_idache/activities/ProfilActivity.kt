package com.example.iuam_idache.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R
import com.example.iuam_idache.apiREST.models.User
import com.example.iuam_idache.classes.GenderType
import io.ghyeok.stickyswitch.widget.StickySwitch
import com.example.iuam_idache.apiREST.classes.ClientRestAPI
import com.example.iuam_idache.apiREST.interfaces.getLongCallback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId

class ProfilActivity : AppCompatActivity() {

    private var newUser = true;

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
            newUser = false;
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

        //----------------------------- Clear data -------------------------------
        // prevent the user from writing a 0 as the first caracter
        weightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == 1 && s.toString().startsWith("0")) {
                    s.clear()
                }
            }
        })

        // prevent the user from writing a 0 as the first caracter
        heightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == 1 && s.toString().startsWith("0")) {
                    s.clear()
                }
            }
        })

        //------------------------------- Buttons --------------------------------
        // Next button
        btnNext = findViewById(R.id.profil_button_next)
        btnNext.setOnClickListener {

            // Check the datas
            // Recover the string
            val birthDate = birthDayEditText.text.toString() + "/" + birthMonthEditText.text.toString() + "/" + birthYearEditText.text.toString()
            val validator = DateValidatorUsingDateFormat("dd/MM/yyyy")

            if (validator.isValid(birthDate) && birthYearEditText.text.toString().toInt() > 1900) {

                val birthDate2 = birthYearEditText.text.toString() + "-" + birthMonthEditText.text.toString() + '-' + birthDayEditText.text.toString()
                // Check if before today

                // TODO : control day & month (we need to enter the 0 for the month or the day.
                //  Ex : month mars = 3 ERROR need 03)
                if (LocalDate.parse(birthDate2).isBefore(LocalDate.now(ZoneId.of("Africa/Tunis")))) {

                    // Gender
                    when (btnGender.getDirection()) {
                        StickySwitch.Direction.LEFT -> editor.putString(
                            userGenderKey,
                            GenderType.MAN
                        )
                        StickySwitch.Direction.RIGHT -> editor.putString(
                            userGenderKey,
                            GenderType.WOMAN
                        )
                        else -> throw IllegalArgumentException("Error in gender selection")
                    }

                    // (TODO control if height and weight are numbuer)
                    // Save the data in shared preferences
                    editor.putString(userBirthDayKey, birthDayEditText.text.toString())
                    editor.putString(userBirthMonthKey, birthMonthEditText.text.toString())
                    editor.putString(userBirthYearKey, birthYearEditText.text.toString())
                    editor.putString(userHeightKey, heightEditText.text.toString())
                    editor.putString(userWeightKey, weightEditText.text.toString())
                    editor.apply()



                    // Build the user
                    var user = buildMyUser();

                    // TODO : check if is a update or new profil with ID in shared preferences
                    if (isUpdateProfil()){

                        val myClientRestAPI = ClientRestAPI()
                        myClientRestAPI.updateUser(user, object : getLongCallback {
                            override fun onSuccess(myID: Long) {
                                // TODO : add notif (update OK)
                                Log.v(
                                    "TAG",
                                    "i update user (id=" + myID + ") " + user.toString()
                                )
                            }

                            override fun onFailure() {
                                Log.v("TAG", "Update User -> Failed ! ")
                                // TODO : add notif (update Failed)
                            }
                        })

                    } else {
                        val myClientRestAPI = ClientRestAPI()
                        myClientRestAPI.addUser(user, object : getLongCallback {
                            override fun onSuccess(myID: Long) {
                                // TODO : add notif
                                // TODO : save id in sharedPreferences
                                user.user_id = myID
                                Log.v("TAG", "i get my id $user")
                            }

                            override fun onFailure() {
                                Log.v("TAG", "Add new User -> Failed ! ")
                                // TODO : add notif
                            }
                        })
                    }

                    // Go to main menu
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    // (TODO : maybe not the day but month or year problem)
                    birthDayEditText.error = "this date cannot be later than today"
                }
            }
            else {
                if (birthDayEditText.text.toString().toInt() > 31) {
                    birthDayEditText.error = "Invalid date"
                }
                if (birthMonthEditText.text.toString().toInt() > 12) {
                    birthMonthEditText.error = "Invalid date"
                }
                if (birthYearEditText.text.toString().toInt() < 1900)  {
                    birthYearEditText.error = "Invalid date"
                }
                else {
                    birthDayEditText.error = "Invalid date"
                    birthMonthEditText.error = "Invalid date"
                }
            }
        }
    }

    // Build the user
    private fun buildMyUser(): User {
        var sexe : Byte = 0;
        when(sharedPreferences.getString(userGenderKey, GenderType.MAN)) {
            GenderType.WOMAN -> sexe = 1;
            GenderType.MAN -> sexe = 0;
        }
        val height = heightEditText.text.toString().toInt()
        val weight = weightEditText.text.toString().toInt()
        var user = User(
            birthYearEditText.text.toString(),
            birthMonthEditText.text.toString(),
            birthDayEditText.text.toString(),
            height,
            sexe,
            weight
        );

        // TODO get my real id if exist
        user.user_id = 30;

        return user
    }

    // TODO check if user ID is in sharedPreferences
    private fun isUpdateProfil(): Boolean {
        return !newUser;
    }

    interface DateValidator {
        fun isValid(dateStr: String?): Boolean
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    class DateValidatorUsingDateFormat(private val dateFormat: String) :
        DateValidator {
        @SuppressLint("SimpleDateFormat")
        override fun isValid(dateStr: String?): Boolean {
            val sdf = SimpleDateFormat(dateFormat)
            sdf.isLenient = false
            try {
                sdf.parse(dateStr)
            } catch (e: ParseException) {
                return false
            }
            return true
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