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



    init{
        Log.i("SignOptionsViewModel", "SignOptionsViewModel created.")




    }

    //FUNCTIONS

    override fun onCleared() {
        super.onCleared()
    }

    fun eventSubmit(){



    }

    fun submitComplete(){

        _eventSubmit.value = false
    }



}