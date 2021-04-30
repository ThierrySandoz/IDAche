package com.example.iuam_idache.apiREST.models

import java.sql.Timestamp

class EventsAche {
    var event_id: Long = -1
    var event_timestamp: Timestamp? = null
    var event_geoloc_lat = -1f
    var event_geoloc_lon = -1f
    var event_localisation = ""
    var event_temp = -1
    var user_id: Long = -1
    var event_humidity = -1
    var event_visibility = -1
    var event_wind_speed = -1

    //String event_code_weather = null;
    var event_code_weather: String = ""
    var event_pressure = -1
    var event_HR_ave = -1
    var event_HR_max = -1
    var event_HR_min = -1
    var event_ACC_activity = -1
    var event_ACC_sleep = -1
    var event_Ache_locality: Byte = -1
    var event_Ache_power: Byte = -1
    var event_Ache_type: Byte = -1

    // With all
    constructor(
        event_geoloc_lat: Float,
        event_geoloc_lon: Float,
        event_localisation: String,
        event_temp: Int,
        user_id: Long,
        event_humidity: Int,
        event_visibility: Int,
        event_wind_speed: Int,
        event_code_weather: String,
        event_pressure: Int,
        event_hr_ave: Int,
        event_hr_max: Int,
        event_hr_min: Int,
        event_acc_activity: Int,
        event_acc_sleep: Int,
        event_ache_locality: Int,
        event_ache_power: Int,
        event_ache_type: Int
    ) {
        this.event_geoloc_lat = event_geoloc_lat
        this.event_geoloc_lon = event_geoloc_lon
        this.event_localisation = event_localisation
        this.event_temp = event_temp
        this.user_id = user_id
        this.event_humidity = event_humidity
        this.event_visibility = event_visibility
        this.event_wind_speed = event_wind_speed
        this.event_code_weather = event_code_weather
        this.event_pressure = event_pressure
        event_HR_ave = event_hr_ave
        event_HR_max = event_hr_max
        event_HR_min = event_hr_min
        event_ACC_activity = event_acc_activity
        event_ACC_sleep = event_acc_sleep
        event_Ache_locality = event_ache_locality.toByte()
        event_Ache_power = event_ache_power.toByte()
        event_Ache_type = event_ache_type.toByte()
    }

    // Without position -> without weather AND Without bluetooth / Polar
    // Only with question sheet
    constructor(
        user_id: Long,
        event_ache_locality: Int,
        event_ache_power: Int,
        event_ache_type: Int
    ) : this(
        -1f,
        -1f,
        "",
        -1,
        user_id,
        -1,
        -1,
        -1,
        "",
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        event_ache_locality,
        event_ache_power,
        event_ache_type
    ) {
    }

    // default (all -1)  Without position -> without weather AND Without bluetooth / Polar
    constructor() {}

    // Without position -> without weather
    constructor(
        user_id: Long,
        event_hr_ave: Int,
        event_hr_max: Int,
        event_hr_min: Int,
        event_acc_activity: Int,
        event_acc_sleep: Int,
        event_ache_locality: Int,
        event_ache_power: Int,
        event_ache_type: Int
    ) : this(
        -1f,
        -1f,
        "",
        -1,
        user_id,
        -1,
        -1,
        -1,
        "",
        -1,
        event_hr_ave,
        event_hr_max,
        event_hr_min,
        event_acc_activity,
        event_acc_sleep,
        event_ache_locality,
        event_ache_power,
        event_ache_type
    ) {
    }

    // Without bluetooth / Polar
    constructor(
        event_geoloc_lat: Float,
        event_geoloc_lon: Float,
        event_localisation: String,
        event_temp: Int,
        user_id: Long,
        event_humidity: Int,
        event_visibility: Int,
        event_wind_speed: Int,
        event_code_weather: String,
        event_pressure: Int,
        event_ache_locality: Int,
        event_ache_power: Int,
        event_ache_type: Int
    ) : this(
        event_geoloc_lat,
        event_geoloc_lon,
        event_localisation,
        event_temp,
        user_id,
        event_humidity,
        event_visibility,
        event_wind_speed,
        event_code_weather,
        event_pressure,
        -1,
        -1,
        -1,
        -1,
        -1,
        event_ache_locality,
        event_ache_power,
        event_ache_type
    ) {
    }

    // with only user ID
    constructor(user_id: Long) : this(
        -1f,
        -1f,
        "",
        -1,
        user_id,
        -1,
        -1,
        -1,
        "",
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1
    ) {
    }

    override fun toString(): String {
        return """ID: $event_id timestamp: $event_timestamp event_geoloc_lat: $event_geoloc_lat event_geoloc_lon: $event_geoloc_lon event_localisation: $event_localisation event_temp: $event_temp user_id: $user_id event_humidity: $event_humidity event_visibility: $event_visibility event_wind_speed $event_wind_speed event_code_weather: $event_code_weather event_pressure: $event_pressure event_HR_ave: $event_HR_ave event_HR_max: $event_HR_max event_HR_min: $event_HR_min event_ACC_activity: $event_ACC_activity event_ACC_sleep: $event_ACC_sleep event_Ache_locality: $event_Ache_locality event_Ache_power: $event_Ache_power event_Ache_power: $event_Ache_power event_Ache_type: $event_Ache_type
"""
    }
}