package com.example.android.navigation.screens.sign_options

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.navigation.database.SignDatabaseDao


/**
 * ViewModel containing all the logic needed to run the sign_options
 */
class SignOptionsViewModel(area : String, type : String, dataSource: SignDatabaseDao, application: Application) : ViewModel() {

//TODO Area and type to Boolean

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