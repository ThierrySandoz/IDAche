package com.example.iuam_idache.classes

import java.net.IDN

class HistoryUnit(
    val date                : String,
    val hour                : String,
    val hearthBeat          : String,
    val hearthBeatMin       : String,
    val hearthBeatMax       : String,
    val hearthBeatAverage   : String,
    val windSpeed           : String,
    val pressure            : String,
    val humidity            : String,
    val temperature         : String,
    val meteoID             : Int,
    val location            : String,
)