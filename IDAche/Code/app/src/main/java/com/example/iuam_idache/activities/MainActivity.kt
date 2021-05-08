package com.example.iuam_idache.activities

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.iuam_idache.R
import com.example.iuam_idache.classes.CallbackPolar
import com.example.iuam_idache.classes.Polar0H1
import com.example.iuam_idache.classes.WeatherIcons
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kwabenaberko.openweathermaplib.constant.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import java.time.Duration
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

var firstLauchBLE = true;

//-------------- Polar variables
private lateinit var pola0H1: Polar0H1

class MainActivity : AppCompatActivity() {

    //-------------- interval time for process ACC in ms (WARNING : in correlation with time increment of activity time & rest time)
    private val INTERVAL_TIME_ACC_PROCESS:Long = 1000
    //-------------- Threashold for pass from rest to activity
    private val AVERAGE_ACC_THRESHOLD = 150

    //-------------- List of values of the 3 accelerometer axes
    var ACC_saved_x = mutableListOf<Int>()
    var ACC_saved_y = mutableListOf<Int>()
    var ACC_saved_z = mutableListOf<Int>()

    //-------------- Duration that the user is in rest or in activity
    var durationActivity: Duration = Duration.ofMillis(0)
    var durationRest: Duration = Duration.ofMillis(0)

    //-------------- Weather variables
    private lateinit var actualWeather: CurrentWeather

    //-------------- Mode bluetooth
    /** true : for enable communication with Polar 0H1 **/
    val BLE_MODE = true

    //--------------  List of N last measure of HR
    var HR_saved = mutableListOf<Int>()
    val N_LAST_MEASURE_HR = 300;



    //-------------- Buttons
    private lateinit var historyButton : ImageButton
    private lateinit var profilButton : ImageButton
    private lateinit var headacheButton : FloatingActionButton

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
    private lateinit var accelXTextView: TextView
    private lateinit var accelYTextView: TextView
    private lateinit var accelZTextView: TextView
    private lateinit var activityTimeTextView: TextView
    private lateinit var restTimeTextView: TextView
    private lateinit var activityTimeUnitTextView: TextView
    private lateinit var restTimeUnitTextView: TextView

    //-------------- ImageViews
    private lateinit var meteoStateImageView : ImageView
    private lateinit var hearthbeatImageView : ImageView

    //-------------- Animation
    private var isAnimationStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //----------------------------- const array -------------------------------
        // Initialize the constant array of meteo icons
        initializeMeteoIconsArray()

        //------------------------------ TextViews --------------------------------
        // Location text view
        locationTextView = findViewById(R.id.activity_main_meteo_textView_location)

        // Scroll the text if too long
        locationTextView.postDelayed(Runnable {
            locationTextView.maxLines = 1
            locationTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
            locationTextView.marqueeRepeatLimit = 10000
            locationTextView.isSelected = true
        }, 5000)

        // Meteo State text view
        meteoStateTextView = findViewById(R.id.activity_main_meteo_textView_meteoState)

        // Scroll the text if too long
        meteoStateTextView.postDelayed(Runnable {
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

        // Accel x text view
        accelXTextView = findViewById(R.id.activity_main_accel_textView_x)

        // Accel y text view
        accelYTextView = findViewById(R.id.activity_main_accel_textView_y)

        // Accel z text view
        accelZTextView = findViewById(R.id.activity_main_accel_textView_z)

        // Activity time text view
        activityTimeTextView = findViewById(R.id.activity_main_Activity_textView_time)
        // Rest time text view
        restTimeTextView = findViewById(R.id.activity_main_Rest_textView_time)

        // Activity time unit text view
        activityTimeUnitTextView = findViewById(R.id.activity_main_Activity_textView_unit)
        // Rest time unit text view
        restTimeUnitTextView = findViewById(R.id.activity_main_Activity_textView_unit2)


        //------------------------------ ImageViews --------------------------------
        // Meteo state image
        meteoStateImageView = findViewById(R.id.activity_main_meteo_imageView_meteoState)

        // Heartbeat animation
        hearthbeatImageView = findViewById(R.id.activity_main_medical_imageView_hearthbeat)

        val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            hearthbeatImageView,
            PropertyValuesHolder.ofFloat("scaleX", 0.8f),
            PropertyValuesHolder.ofFloat("scaleY", 0.8f)
        )
        scaleDown.duration = 300

        scaleDown.repeatCount = ObjectAnimator.INFINITE
        scaleDown.repeatMode = ObjectAnimator.REVERSE

        //----------------------- Check permissions for localisation ------------------------
        // Check permissions
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_DENIED
        ) {
            // Permission denied
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            // Show pop up to request permission
            requestPermissions(permissions, PERMISSION_CODE_LOCATION)
        } else {
            // Permission granted
            setLocation()
        }


        // Process each INTERVAL_TIME_ACC_PROCESS in ms the acc values
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {

                var standardDeviation = mutableListOf<Int>(0,0,0)

                Log.d("TEST","ONE SECOND PASSED !!!!!!  \t")

                // Stop the animation
                if (isAnimationStarted && !pola0H1.connected) {
                    scaleDown.end()
                    isAnimationStarted = false
                }

                // is there is new values
                if (ACC_saved_x.size != 0) {
                    // save values & clear saved buffer
                    val ACC_x = ACC_saved_x.toMutableList()
                    ACC_saved_x.clear()
                    val ACC_y = ACC_saved_y.toMutableList()
                    ACC_saved_y.clear()
                    val ACC_z = ACC_saved_z.toMutableList()
                    ACC_saved_z.clear()

                    // calcul standard Deviation
                    standardDeviation[0] = calculSD(ACC_x)
                    standardDeviation[1]  = calculSD(ACC_y)
                    standardDeviation[2]  = calculSD(ACC_z)
                    Log.d("TEST","SD Y = $standardDeviation\n")

                    // calcul average of standard Deviation
                    val sdAve = standardDeviation.average()
                    Log.d("TEST","SD ave = $sdAve \n")

                    // Test if user is in activity or in rest
                    if (sdAve > AVERAGE_ACC_THRESHOLD){
                        durationActivity = durationActivity.plusMillis(INTERVAL_TIME_ACC_PROCESS)

                        if (durationActivity.seconds >= 60){
                            if (durationActivity.toMinutes() >= 60 ){
                                activityTimeTextView.text = durationActivity.toHours().toString()
                                activityTimeUnitTextView.text = " h"
                            } else {
                                activityTimeTextView.text = durationActivity.toMinutes().toString()
                                activityTimeUnitTextView.text = " min"
                            }
                        } else {
                            activityTimeTextView.text = durationActivity.seconds.toString()
                            activityTimeUnitTextView.text = " sec"
                        }

                        Log.d("TEST","in Activity ! (duration = ${durationActivity.seconds})\n")
                    } else {
                        durationRest = durationRest.plusMillis(INTERVAL_TIME_ACC_PROCESS)
                        if (durationRest.seconds >= 60){
                            if (durationActivity.toMinutes() >= 60 ){
                                restTimeTextView.text = durationRest.toHours().toString()
                                restTimeUnitTextView.text = " h"
                            } else {
                                restTimeTextView.text = durationRest.toMinutes().toString()
                                restTimeUnitTextView.text = " min"
                            }
                        } else {
                            restTimeTextView.text = durationRest.seconds.toString()
                            restTimeUnitTextView.text = " sec"


                        }

                        Log.d("TEST","in Rest ! (duration = ${durationRest.seconds})\n")
                    }

                    // Set the activity and rest time to the companion object
                    activityTimeValue = durationActivity.seconds
                    restTimeValue = durationRest.seconds
                }

                mainHandler.postDelayed(this, INTERVAL_TIME_ACC_PROCESS)
            }
        })

        // Interface (Callback via la classe Polar0H1)
        val myPolarCB: CallbackPolar = object : CallbackPolar {
            override fun getHr(hr: Int) {

                // Start the animation
                if (!isAnimationStarted) {
                    scaleDown.start()
                    isAnimationStarted = true
                }

                // add measure at list (if not 0)
                if (hr != 0){
                    HR_saved.add(0, hr)
                    if (HR_saved.size > N_LAST_MEASURE_HR)
                        HR_saved.removeAt(N_LAST_MEASURE_HR)
                }

                // Calcul values max / min / average
                val HR_ave = HR_saved.average()
                val HR_max = HR_saved.maxOrNull()
                val HR_min = HR_saved.minOrNull()

                // Set values to the companion object
                if (HR_ave.toInt() != 0) {
                    heartRateAverageValue = HR_ave.toInt()
                }
                if (HR_min != null) {
                    heartRateMinValue = HR_min
                }
                if (HR_max != null) {
                    heartRateMaxValue = HR_max
                }

                // Print valus
                hearthBeatMinTextView.text = HR_min.toString()
                hearthBeatAverageTextView.text = String.format("%.1f", HR_ave)
                hearthBeatMaxTextView.text = HR_max.toString()

                Log.v("TAG", "GET HR : $hr[bpm] \n");

                if ( !pola0H1.ACCisStreamed() && pola0H1.ACCReady) {
                    Log.d("DBG", "ACCisStreamed = false");
                    pola0H1.getStreamACC();
                }

                // Set the data to the visualisation
                hearthBeatTextView.text = hr.toString()
            }




            override fun getACC(x: Int, y: Int, z: Int) {
//                Log.v("TAG", "GET ACC : x=$x y=$y z=$z\n");
//
//                // Set the data to the visualisation
//                accelXTextView.text = x.toString()
//                accelYTextView.text = y.toString()
//                accelZTextView.text = z.toString()

                ACC_saved_x.add(x)
                ACC_saved_y.add(y)
                ACC_saved_z.add(z)
            }
        }
        if(!firstLauchBLE){
            pola0H1.cb = myPolarCB
            pola0H1.connect()
        }

        //------------------------------- CONNECT WITH POLAR --------------------------------
        /** Communiacte with Polar 0H1 **/
        if(BLE_MODE && firstLauchBLE){
            firstLauchBLE = false
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)


            pola0H1 = Polar0H1(myPolarCB, "7D41F628", this)
            pola0H1.init()
            pola0H1.connect()

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
            val intent = Intent(this, SelectSymptomActivity::class.java)
            startActivity(intent)
        }
    }

    private fun calculSD(accX: MutableList<Int>): Int {

        val size = accX.size

        val somme = accX.sum()
        val mean = somme / size
        var standardDeviation = 0;

        for (x in accX) {
            standardDeviation += ((x - mean).toDouble().pow(2.0)).toInt()
        }
        return sqrt((standardDeviation / size).toDouble()).toInt()

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
        else if (requestCode == PERMISSION_CODE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission from popup granted
                setLocation()
            } else {
                // Permission from popup was denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                //------------------------------- GET WEATHER --------------------------------
                val helper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
                helper.setUnits(Units.METRIC)

                helper.getCurrentWeatherByCityName(
                    "Yverdon-les-Bains",
                    object : CurrentWeatherCallback {
                        override fun onSuccess(currentWeather: CurrentWeather) {
                            // Get current weather
                            actualWeather = currentWeather

                            val weatherId = weatherIconsList.findLast { weatherIcons ->
                                // Check if night and ID > 800
                                if (actualWeather.weather[0].icon.toString().contains(
                                        "n",
                                        ignoreCase = true
                                    ) && (actualWeather.weather[0].id.toInt() >= 800)
                                ) {
                                    // If night and ID > 800 -> Add 100 to the ID
                                    weatherIcons.iconOpenWeatherID == actualWeather.weather[0].id.toInt() + 100
                                } else {
                                    weatherIcons.iconOpenWeatherID == actualWeather.weather[0].id.toInt()
                                }
                            }

                            if (weatherId != null) {
                                meteoStateTextView.text = weatherId.description
                                meteoStateImageView.setImageResource(weatherId.iconDrawableID)
                            } else {
                                meteoStateTextView.text = "Unknown"
                                meteoStateImageView.setImageResource(R.drawable.ic_weather_icon_cloudy)
                            }

                            // Display infos to screen
                            locationTextView.text = actualWeather.name.toString()
                            temperatureTextView.text = actualWeather.main.temp.toInt().toString()
                            humidityTextView.text = actualWeather.main.humidity.toString()
                            pressureTextView.text = actualWeather.main.pressure.toInt().toString()
                            windSpeedTextView.text = actualWeather.wind.speed.toInt().toString()

                            // Set the companion object values
                            humidityValue = actualWeather.main.humidity
                            windSpeedValue = actualWeather.wind.speed
                            temperatureValue = actualWeather.main.temp
                            pressureValue = actualWeather.main.pressure
                            localisationValue = actualWeather.name
                            latitudeValue = actualWeather.coord.lat
                            longitudeValue = actualWeather.coord.lon
                            if (weatherId != null) {
                                meteoIdValue = weatherId.iconOpenWeatherID
                            }

                            /** TEST PRINT MAX INFO **/
                            Log.v(
                                "TAG_WEATHER",
                                """
        Coordinates: ${actualWeather.coord.lat}, ${actualWeather.coord.lon}
        Weather Description: ${actualWeather.weather[0].description}
        Temp. Max: ${actualWeather.main.tempMax}
        Wind Speed: ${actualWeather.wind.speed}
        City, Country: ${actualWeather.name}, ${actualWeather.sys.country}
        """.trimIndent()
                            )

                            Log.v(
                                "TAG_WEATHER", """
     getClouds: ${actualWeather.clouds.all}
     getHumidity: ${actualWeather.main.humidity}
     getPressure: ${actualWeather.main.pressure}
     getTemp: ${actualWeather.main.temp}
     getTempMin: ${actualWeather.main.tempMin}
     getFeelsLike: ${actualWeather.main.feelsLike}
     getGrndLevel: ${actualWeather.main.grndLevel}
     getVisibility: ${actualWeather.visibility}
     getRain: ${actualWeather.rain}
     size: ${actualWeather.weather.size}
     getIcon: ${actualWeather.weather[0].icon}
     getMain: ${actualWeather.weather[0].main}
     
     """.trimIndent()
                            )
                        }

                        override fun onFailure(throwable: Throwable) {
                            Log.v("TAG_WEATHER", throwable.message!!)
                        }
                    })
            }
        }
    }

    //----------------------------------- ShareLocation --------------------------------------------
    private fun setLocation() {

        lateinit var fusedLocationProviderClient: FusedLocationProviderClient

        // Check permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission denied -> Nothing to do since we already prompt the user for permissions
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
        }
        else { // Permission granted -> Share location

            // Get the location provider
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            // Get the last location
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {

                val helper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
                helper.setUnits(Units.METRIC)

                // Check if location is not null
                if (it != null) {

                    // Get latitude and longitude
                    val latitude = it.latitude
                    val longitude = it.longitude

                    helper.getCurrentWeatherByGeoCoordinates(
                        latitude,
                        longitude,
                        object : CurrentWeatherCallback {
                            override fun onSuccess(currentWeather: CurrentWeather) {
                                // Get current weather
                                actualWeather = currentWeather

                                val weatherId = weatherIconsList.findLast { weatherIcons ->
                                    // Check if night and ID > 800
                                    if (actualWeather.weather[0].icon.toString().contains(
                                            "n",
                                            ignoreCase = true
                                        ) && (actualWeather.weather[0].id.toInt() >= 800)
                                    ) {
                                        // If night and ID > 800 -> Add 100 to the ID
                                        weatherIcons.iconOpenWeatherID == actualWeather.weather[0].id.toInt() + 100
                                    } else {
                                        weatherIcons.iconOpenWeatherID == actualWeather.weather[0].id.toInt()
                                    }
                                }

                                if (weatherId != null) {
                                    meteoStateTextView.text = weatherId.description
                                    meteoStateImageView.setImageResource(weatherId.iconDrawableID)
                                } else {
                                    meteoStateTextView.text = "Unknown"
                                    meteoStateImageView.setImageResource(R.drawable.ic_weather_icon_cloudy)
                                }

                                // Display infos to screen
                                locationTextView.text = actualWeather.name.toString()
                                temperatureTextView.text = actualWeather.main.temp.toInt().toString()
                                humidityTextView.text = actualWeather.main.humidity.toString()
                                pressureTextView.text = actualWeather.main.pressure.toInt().toString()
                                windSpeedTextView.text = actualWeather.wind.speed.toInt().toString()

                                // Set the companion object values
                                humidityValue = actualWeather.main.humidity
                                windSpeedValue = actualWeather.wind.speed
                                temperatureValue = actualWeather.main.temp
                                pressureValue = actualWeather.main.pressure
                                localisationValue = actualWeather.name
                                latitudeValue = actualWeather.coord.lat
                                longitudeValue = actualWeather.coord.lon
                                if (weatherId != null) {
                                    meteoIdValue = weatherId.iconOpenWeatherID
                                }
                            }

                            override fun onFailure(throwable: Throwable) {
                                Log.v("TAG_WEATHER", throwable.message!!)
                            }
                        })
                }
                else {
                    Toast.makeText(this, "The location could not be obtained...", Toast.LENGTH_LONG).show()

                    helper.getCurrentWeatherByCityName(
                        "Yverdon-les-Bains",
                        object : CurrentWeatherCallback {
                            override fun onSuccess(currentWeather: CurrentWeather) {
                                // Get current weather
                                actualWeather = currentWeather

                                val weatherId = weatherIconsList.findLast { weatherIcons ->
                                    // Check if night and ID > 800
                                    if (actualWeather.weather[0].icon.toString().contains(
                                            "n",
                                            ignoreCase = true
                                        ) && (actualWeather.weather[0].id.toInt() >= 800)
                                    ) {
                                        // If night and ID > 800 -> Add 100 to the ID
                                        weatherIcons.iconOpenWeatherID == actualWeather.weather[0].id.toInt() + 100
                                    } else {
                                        weatherIcons.iconOpenWeatherID == actualWeather.weather[0].id.toInt()
                                    }
                                }

                                if (weatherId != null) {
                                    meteoStateTextView.text = weatherId.description
                                    meteoStateImageView.setImageResource(weatherId.iconDrawableID)
                                } else {
                                    meteoStateTextView.text = "Unknown"
                                    meteoStateImageView.setImageResource(R.drawable.ic_weather_icon_cloudy)
                                }

                                // Display infos to screen
                                locationTextView.text = actualWeather.name.toString()
                                temperatureTextView.text = actualWeather.main.temp.toInt().toString()
                                humidityTextView.text = actualWeather.main.humidity.toString()
                                pressureTextView.text = actualWeather.main.pressure.toInt().toString()
                                windSpeedTextView.text = actualWeather.wind.speed.toInt().toString()

                                // Set the companion object values
                                humidityValue = actualWeather.main.humidity
                                windSpeedValue = actualWeather.wind.speed
                                temperatureValue = actualWeather.main.temp
                                pressureValue = actualWeather.main.pressure
                                localisationValue = actualWeather.name
                                latitudeValue = actualWeather.coord.lat
                                longitudeValue = actualWeather.coord.lon
                                if (weatherId != null) {
                                    meteoIdValue = weatherId.iconOpenWeatherID
                                }


                            }

                            override fun onFailure(throwable: Throwable) {
                                Log.v("TAG_WEATHER", throwable.message!!)
                            }
                        })
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        /** Need for api ble **/
        if(BLE_MODE){
            pola0H1.backgroundEntered()
        }
    }

    override fun onStart() {
        super.onStart()
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

    fun initializeMeteoIconsArray() {
        weatherIconsList = arrayListOf(
            WeatherIcons(200, R.drawable.ic_weather_icon_thunder, "thunderstorm with light rain"),
            WeatherIcons(201, R.drawable.ic_weather_icon_thunder, "thunderstorm with rain"),
            WeatherIcons(202, R.drawable.ic_weather_icon_thunder, "thunderstorm with heavy rain"),
            WeatherIcons(210, R.drawable.ic_weather_icon_thunder, "light thunderstorm"),
            WeatherIcons(211, R.drawable.ic_weather_icon_thunder, "thunderstorm"),
            WeatherIcons(212, R.drawable.ic_weather_icon_thunder, "heavy thunderstorm"),
            WeatherIcons(221, R.drawable.ic_weather_icon_thunder, "ragged thunderstorm"),
            WeatherIcons(230, R.drawable.ic_weather_icon_thunder, "thunderstorm with light drizzle"),
            WeatherIcons(231, R.drawable.ic_weather_icon_thunder, "thunderstorm with drizzle"),
            WeatherIcons(232, R.drawable.ic_weather_icon_thunder, "thunderstorm with heavy drizzle"),

            WeatherIcons(300, R.drawable.ic_weather_icon_rainy_4, "light intensity drizzle"),
            WeatherIcons(301, R.drawable.ic_weather_icon_rainy_4, "drizzle"),
            WeatherIcons(302, R.drawable.ic_weather_icon_rainy_4, "heavy intensity drizzle"),
            WeatherIcons(310, R.drawable.ic_weather_icon_rainy_5, "light intensity drizzle rain"),
            WeatherIcons(311, R.drawable.ic_weather_icon_rainy_5, "drizzle rain"),
            WeatherIcons(312, R.drawable.ic_weather_icon_rainy_5, "heavy intensity drizzle rain"),
            WeatherIcons(313, R.drawable.ic_weather_icon_rainy_6, "shower rain and drizzle"),
            WeatherIcons(314, R.drawable.ic_weather_icon_rainy_6, "heavy shower rain and drizzle"),
            WeatherIcons(321, R.drawable.ic_weather_icon_rainy_6, "shower drizzle"),

            WeatherIcons(500, R.drawable.ic_weather_icon_rainy_2, "light rain"),
            WeatherIcons(501, R.drawable.ic_weather_icon_rainy_2, "moderate rain"),
            WeatherIcons(502, R.drawable.ic_weather_icon_rainy_2, "heavy intensity rain"),
            WeatherIcons(503, R.drawable.ic_weather_icon_rainy_3, "very heavy rain"),
            WeatherIcons(504, R.drawable.ic_weather_icon_rainy_3, "extreme rain"),
            WeatherIcons(511, R.drawable.ic_weather_icon_rainy_7, "freezing rain"),
            WeatherIcons(520, R.drawable.ic_weather_icon_rainy_4, "light intensity shower rain"),
            WeatherIcons(521, R.drawable.ic_weather_icon_rainy_5, "shower rain"),
            WeatherIcons(522, R.drawable.ic_weather_icon_rainy_5, "heavy intensity shower rain"),
            WeatherIcons(531, R.drawable.ic_weather_icon_rainy_6, "ragged shower rain"),

            WeatherIcons(600, R.drawable.ic_weather_icon_snowy_4, "light snow"),
            WeatherIcons(601, R.drawable.ic_weather_icon_snowy_5, "Snow"),
            WeatherIcons(602, R.drawable.ic_weather_icon_snowy_6, "Heavy snow"),
            WeatherIcons(611, R.drawable.ic_weather_icon_snowy_2, "Sleet"),
            WeatherIcons(612, R.drawable.ic_weather_icon_snowy_2, "Light shower sleet"),
            WeatherIcons(613, R.drawable.ic_weather_icon_snowy_3, "Shower sleet"),
            WeatherIcons(615, R.drawable.ic_weather_icon_snowy_4, "Light rain and snow"),
            WeatherIcons(616, R.drawable.ic_weather_icon_snowy_5, "Rain and snow"),
            WeatherIcons(620, R.drawable.ic_weather_icon_snowy_4, "Light shower snow"),
            WeatherIcons(621, R.drawable.ic_weather_icon_snowy_5, "Shower snow"),
            WeatherIcons(622, R.drawable.ic_weather_icon_snowy_6, "Heavy shower snow"),

            WeatherIcons(701, R.drawable.ic_weather_icon_mist, "mist"),
            WeatherIcons(711, R.drawable.ic_weather_icon_mist, "Smoke"),
            WeatherIcons(721, R.drawable.ic_weather_icon_mist, "Haze"),
            WeatherIcons(731, R.drawable.ic_weather_icon_mist, "sand/ dust whirls"),
            WeatherIcons(741, R.drawable.ic_weather_icon_mist, "fog"),
            WeatherIcons(751, R.drawable.ic_weather_icon_mist, "sand"),
            WeatherIcons(761, R.drawable.ic_weather_icon_mist, "dust"),
            WeatherIcons(762, R.drawable.ic_weather_icon_mist, "volcanic ash"),
            WeatherIcons(771, R.drawable.ic_weather_icon_mist, "squalls"),
            WeatherIcons(781, R.drawable.ic_weather_icon_mist, "tornado"),

            WeatherIcons(800, R.drawable.ic_weather_icon_day, "clear sky"),
            WeatherIcons(801, R.drawable.ic_weather_icon_cloudy_day_1, "few clouds (11-25%)"),
            WeatherIcons(802, R.drawable.ic_weather_icon_cloudy_day_2, "scattered clouds (25-50%)"),
            WeatherIcons(803, R.drawable.ic_weather_icon_cloudy_day_3, "broken clouds (51-84%)"),
            WeatherIcons(804, R.drawable.ic_weather_icon_cloudy, "overcast clouds (85-100%)"),

            WeatherIcons(900, R.drawable.ic_weather_icon_night, "clear sky"),
            WeatherIcons(901, R.drawable.ic_weather_icon_cloudy_night_1, "few clouds (11-25%)"),
            WeatherIcons(902, R.drawable.ic_weather_icon_cloudy_night_2, "scattered clouds (25-50%)"),
            WeatherIcons(903, R.drawable.ic_weather_icon_cloudy_night_3, "broken clouds (51-84%)"),
            WeatherIcons(904, R.drawable.ic_weather_icon_cloudy, "overcast clouds (85-100%)"),
        )
    }

    companion object {
        const val PERMISSION_CODE_LOCATION = 1001
        var heartRateAverageValue : Int = -1
        var heartRateMinValue : Int = -1
        var heartRateMaxValue : Int = -1
        var activityTimeValue : Long = -1
        var restTimeValue : Long = -1
        var humidityValue : Double = -1.0
        var windSpeedValue : Double = -1.0
        var meteoIdValue : Int = -1
        var latitudeValue : Double = -1.0
        var longitudeValue : Double = -1.0
        var temperatureValue : Double = -1.0
        var pressureValue : Double = -1.0
        var localisationValue : String = "None"
        var weatherIconsList : ArrayList<WeatherIcons> = arrayListOf()
    }

}