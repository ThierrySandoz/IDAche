package com.example.iuam_idache.classes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NumberPickerSharedViewModel : ViewModel() {
    val selectedItem = MutableLiveData<Int>()
    var actualPage : HeadachePages = HeadachePages.PAIN_LEVEL
    fun onItemSelected(item: Int) {
        selectedItem.value = item
    }
}