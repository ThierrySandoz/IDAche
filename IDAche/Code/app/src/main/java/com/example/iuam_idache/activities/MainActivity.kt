package com.example.iuam_idache.activities

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import java.util.*

// TODO handle connexion with Polar
var firstLauchBLE = true;

//-------------- Polar variables
private lateinit var pola0H1: Polar0H1

// TODO : handle langage
class MainActivity : AppCompatActivity() {

    //-------------- Weather variables
    private lateinit var actualWeather: CurrentWeather

    /** true : for communiacte with Polar 0H1 **/
    val BLE_MODE = true

    // List of N last measure of HR TODO : maybe opti with LinkedList
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

    //-------------- ImageViews
    private lateinit var meteoStateImageView : ImageView
    private lateinit var hearthbeatImageView : ImageView

    //-------------- Animation
    private var isAnimationStarted = false

    //-------------- List of meteoIcons
    private val weatherIconsList : ArrayList<WeatherIcons> = arrayListOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        //------------------------------ ImageViews --------------------------------
        // Meteo state image
        meteoStateImageView = findViewById(R.id.activity_main_meteo_imageView_meteoState)

        // Heartbeat animation
        hearthbeatImageView = findViewById(R.id.activity_main_medical_imageView_hearthbeat)

        val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            hearthbeatImageView,
            PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f)
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

                // Print valus
                hearthBeatMinTextView.text = HR_min.toString()
                hearthBeatAverageTextView.text = String.format("%.1f", HR_ave)
                hearthBeatMaxTextView.text = HR_max.toString()

                Log.v("TAG", "GET HR : $hr[bpm] \n");

                if ( pola0H1.ACCisStreamed() ) {
                    Log.d("DBG", "ACCisStreamed = true");
                } else {
                    Log.d("DBG", "ACCisStreamed = false");
                    // TODO acc not always OK..
                    pola0H1.getStreamACC();
                }

                // Set the data to the visualisation
                hearthBeatTextView.text = hr.toString()
            }

            override fun getACC(x: Int, y: Int, z: Int) {
                Log.v("TAG", "GET ACC : x=$x y=$y z=$z\n");

                // Set the data to the visualisation
                accelXTextView.text = x.toString()
                accelYTextView.text = y.toString()
                accelZTextView.text = z.toString()
            }
        }
        if(!firstLauchBLE){
            pola0H1.cb = myPolarCB
            pola0H1.connect()
        }

        // TODO -> Stop heartBeat animation when polar disconnected
        //scaleDown.end()
        //isAnimationStarted = false

        //------------------------------- CONNECT WITH POLAR --------------------------------
        /** Communiacte with Polar 0H1 **/
        if(BLE_MODE && firstLauchBLE){
            firstLauchBLE = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && savedInstanceState == null) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }


            pola0H1 = Polar0H1(myPolarCB, "7D41F628", this)
            pola0H1.init()
            pola0H1.connect()

            /*
            Timer("SteamACC", false).schedule(10000) {
                if (pola0H1.BLEPowered && pola0H1.connected){
                    Log.v("TAG", "ASK ACC STREAM NOW !\n");
                    pola0H1.getStreamACC();
                } else {
                    Log.v("TAG", "DONT ASK ACC STREAM NOW !\n");
                }
            }*/
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
                                temperatureTextView.text =
                                    actualWeather.main.temp.toInt().toString()
                                humidityTextView.text = actualWeather.main.humidity.toString()
                                pressureTextView.text =
                                    actualWeather.main.pressure.toInt().toString()
                                windSpeedTextView.text = actualWeather.wind.speed.toInt().toString()
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
                                temperatureTextView.text =
                                    actualWeather.main.temp.toInt().toString()
                                humidityTextView.text = actualWeather.main.humidity.toString()
                                pressureTextView.text =
                                    actualWeather.main.pressure.toInt().toString()
                                windSpeedTextView.text = actualWeather.wind.speed.toInt().toString()
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

    companion object {
        const val PERMISSION_CODE_LOCATION = 1001
    }

}