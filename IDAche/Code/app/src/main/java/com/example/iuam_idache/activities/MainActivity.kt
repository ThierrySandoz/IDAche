package com.example.iuam_idache.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.iuam_idache.R
import com.example.iuam_idache.classes.CallbackPolar
import com.example.iuam_idache.classes.Polar0H1
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    /** true : for communiacte with Polar 0H1 **/
    val BLE_MODE = false

    private lateinit var pola0H1: Polar0H1

    //-------------- Buttons
    private lateinit var historyButton : ImageButton
    private lateinit var profilButton : ImageButton
    private lateinit var headacheButton : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        /** Communiacte with Polar 0H1 **/
        if(BLE_MODE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && savedInstanceState == null) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }

            // Interface (Callback via la classe Polar0H1)
            val myPolarCB: CallbackPolar = object : CallbackPolar {
                override fun getHr(hr: Int) {
                    Log.v("TAG", "GET HR : $hr[bpm] \n");

                }

                override fun getACC(x: Int, y: Int, z: Int) {
                    Log.v("TAG", "GET ACC : x=$x y=$y z=$z\n");
                }
            }

            pola0H1 = Polar0H1(myPolarCB, "7D41F628", this)
            pola0H1.init()
            pola0H1.connect()

            /* TODO : ask stream acc not clean ... */
            Timer("SteamACC", false).schedule(10000) {
                Log.v("TAG", "ASK ACC STREAM NOW !\n");
                pola0H1.getStreamACC();
            }
        }



        //------------------------------- Buttons --------------------------------
        // History button
        historyButton = findViewById(R.id.main_toolbar_imageButton_history)
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // Profil button
        profilButton = findViewById(R.id.main_toolbar_imageButton_profil)
        profilButton.setOnClickListener {
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
        }

        // Headache floating button
        headacheButton = findViewById(R.id.main_floatingButton_headache)
        headacheButton.setOnClickListener {
            val intent = Intent(this, HeadacheActivity::class.java)
            startActivity(intent)
        }

    }



    /** Need for ble **/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            Log.d("TAG", "bt ready")
        }
    }

    override fun onPause() {
        super.onPause()
        /** Need for api ble **/
        if(BLE_MODE){
            pola0H1.backgroundEntered()
        }
    }

    override fun onResume() {
        super.onResume()
        /** Need for api ble **/
        if(BLE_MODE){
            pola0H1.foregroundEntered()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /** Need for api ble **/
        if(BLE_MODE){
            pola0H1.shutdown()
        }
    }

}