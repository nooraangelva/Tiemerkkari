package com.example.android.navigation.screens.sign_type

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignTypeViewModel (area: String) : ViewModel() {



    private val _arrowsChosen = MutableLiveData<Boolean>()
    val arrowsChosen: LiveData<Boolean>
        get() = _arrowsChosen
    private val _speedLimitsChosen = MutableLiveData<Boolean>()
    val speedLimitsChosen: LiveData<Boolean>
        get() = _speedLimitsChosen
    private val _othersChosen = MutableLiveData<Boolean>()
    val othersChosen: LiveData<Boolean>
        get() = _othersChosen
    private val _area = MutableLiveData<String>()
    val area: LiveData<String>
        get() = _area
    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type





    init{
        Log.i("SignTypeViewModel", "SignTypeViewModel created.")


        _area.value = area
        _type.value = ""
        _arrowsChosen.value = false
        _speedLimitsChosen.value = false
        _othersChosen.value = false


    }

    //FUNCTIONS


    fun optionArrowsChosen(){

        _arrowsChosen.value = true
        _type.value = "arrows"

    }

    fun optionArrowsChosenComplete(){

        _arrowsChosen.value = false
    }

    fun optionSpeedLimitsChosen(){

        _speedLimitsChosen.value = true
        _type.value = "speed limit"

    }

    fun optionSpeedLimitsChosenComplete(){

        _speedLimitsChosen.value = false
    }

    fun optionOthersChosen(){

        _arrowsChosen.value = true
        _type.value = "arrows"

    }

    fun optionOthersChosenComplete(){

        _othersChosen.value = false
    }



}