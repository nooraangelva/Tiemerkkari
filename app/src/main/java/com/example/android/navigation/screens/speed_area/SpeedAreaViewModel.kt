package com.example.android.navigation.screens.speed_area

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpeedAreaViewModel (area: String) : ViewModel() {



    private val _cityChosen = MutableLiveData<Boolean>()
    val cityChosen: LiveData<Boolean>
        get() = _cityChosen
    private val _countyChosen = MutableLiveData<Boolean>()
    val countyChosen: LiveData<Boolean>
        get() = _countyChosen
    private val _area = MutableLiveData<String>()
    val area: LiveData<String>
        get() = _area



    init{
        Log.i("SignOptionsViewModel", "SignOptionsViewModel created.")
        _area.value = ""
        _cityChosen.value = false
        _countyChosen.value = false

    }

    //FUNCTIONS

    fun cityChosen(){

        _cityChosen.value = true
        _area.value = "city"

    }

    fun cityChosenComplete(){

        _cityChosen.value = false
    }

    fun countyChosen(){

        _countyChosen.value = true
        _area.value = "county"

    }

    fun countyChosenComplete(){

        _countyChosen.value = false
    }

}