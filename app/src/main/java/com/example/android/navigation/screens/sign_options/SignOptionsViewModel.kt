package com.example.android.navigation.screens.sign_options

import android.util.Log
import androidx.lifecycle.*


/**
 * ViewModel containing all the logic needed to run the sign_options
 */
class SignOptionsViewModel(type: String, area: String) : ViewModel() {



    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type
    private val _area = MutableLiveData<String>()
    val area: LiveData<String>
        get() = _area



    init{
        Log.i("SignOptionsViewModel", "SignOptionsViewModel created.")
        _area.value = area
        _type.value = type
        _eventSubmit.value = false



    }

    //FUNCTIONS

    fun eventSubmit(){

        _eventSubmit.value = true

    }

    fun submitComplete(){

        _eventSubmit.value = false
    }



}