package com.example.iuam_idache.apiREST.models

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

//import java.sql.Date;
class User internal constructor(date: Date?, height: Int, sexe: Byte, weight: Int) {
    var user_id: Long
    var user_birth: Date?
    var user_height: Int
    var user_sexe: Byte
    var user_weight: Int

    internal constructor(
        year: String,
        month: String,
        day: String,
        height: Int,
        sexe: Byte,
        weight: Int
    ) : this(null, height, sexe, weight) {
        try {
            user_birth = SimpleDateFormat("yyyy-MM-dd").parse("$year-$month-$day")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun toString(): String {
        return "ID: " + user_id + " birth: " + user_birth + " user_height: " + user_height + " user_sexe: " + (if (user_sexe.toInt() == 0) "homme" else "femme") + " user_weight: " + user_weight
    }

    init {
        user_id = -1
        user_height = height
        user_sexe = sexe
        user_weight = weight
        user_birth = date
    }
}