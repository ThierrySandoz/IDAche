package com.example.iuam_idache.adapters

import android.util.Log
import com.super_rabbit.wheel_picker.WheelAdapter
import java.util.*

class WPDayPickerAdapter(private val dateList : Array<String>, private val nbDataToDisplay : Int) : WheelAdapter {

    //get item value based on item position in wheel
    override fun getValue(position: Int): String {
        return if ((position < dateList.size-nbDataToDisplay) and (position > -nbDataToDisplay-1)) {
            dateList[position + nbDataToDisplay]
        } else
            ""
    }

    //get item position based on item string value
    override fun getPosition(vale: String): Int {
        // TODO implement for researching in database

        return dateList.indexOf(vale)
    }

    //return a string with the approximate longest text width, for supporting WRAP_CONTENT
    override fun getTextWithMaximumLength(): String {
        return "00/00/0000"
    }

    //return the maximum index
    override fun getMaxIndex(): Int {
        return dateList.size-nbDataToDisplay-1
    }

    //return the minimum index
    override fun getMinIndex(): Int {
        return -nbDataToDisplay
    }
}