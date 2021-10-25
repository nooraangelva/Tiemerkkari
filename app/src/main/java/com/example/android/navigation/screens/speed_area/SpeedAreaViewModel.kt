package com.example.android.navigation.screens.speed_area

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpeedAreaViewModel () : ViewModel() {



    private val _cityChosen = MutableLiveData<Boolean>()
    val cityChosen: LiveData<Boolean>
        get() = _cityChosen
    private val _completeChosen = MutableLiveData<Boolean>()
    val completeChosen: LiveData<Boolean>
        get() = _completeChosen



    init{
        Log.i("SignOptionsViewModel", "SignOptionsViewModel created.")
        _cityChosen.value = false
        _completeChosen.value = true

    }

    //FUNCTIONS

    fun cityChosen(){

        _cityChosen.value = true
        _completeChosen.value = false

    }

    fun cityChosenComplete(){

        _completeChosen.value = true

    }

    fun countyChosen(){

        _cityChosen.value = false
        _completeChosen.value = false

    }

    fun countyChosenComplete(){

        _completeChosen.value = true
    }

}