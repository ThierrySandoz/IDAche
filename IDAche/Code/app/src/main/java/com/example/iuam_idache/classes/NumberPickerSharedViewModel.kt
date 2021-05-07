package com.example.iuam_idache.classes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NumberPickerSharedViewModel : ViewModel() {
    val selectedItem = MutableLiveData<Int>()
    var actualPage = MutableLiveData<HeadachePages>()
    fun onItemSelected(item: Int) {
        selectedItem.value = item
    }
    fun onPageSelected(page : HeadachePages) {
        actualPage.value = page
    }
}