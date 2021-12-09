package com.example.android.navigation.screens.sign_type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

// ViewModel containing all the logic needed to run the signType
class SignTypeViewModel (area: Boolean) : ViewModel() {

    // Status variables for buttons

    private val _arrowsChosen = MutableLiveData<Boolean>()
    val arrowsChosen: LiveData<Boolean>
        get() = _arrowsChosen

    private val _speedLimitsChosen = MutableLiveData<Boolean>()
    val speedLimitsChosen: LiveData<Boolean>
        get() = _speedLimitsChosen

    private val _othersChosen = MutableLiveData<Boolean>()
    val othersChosen: LiveData<Boolean>
        get() = _othersChosen

    // Parameters for signOptions database GET

    private val _area = MutableLiveData<Boolean>()
    val area: LiveData<Boolean>
        get() = _area

    private val _type = MutableLiveData<Int>()
    val type: LiveData<Int>
        get() = _type

    // Initializes when fragment is created
    init{

        Timber.tag("SignTypeViewModel").v("init")

        _area.value = area
        _type.value = 0
        _arrowsChosen.value = false
        _speedLimitsChosen.value = false
        _othersChosen.value = false

    }

    //STATUS CHANGE FUNCTIONS FOR BUTTONS ----------------------------------------------------------

    fun optionArrowsChosen(){

        _arrowsChosen.value = true
        _type.value = 0

    }

    fun optionArrowsChosenComplete(){

        _arrowsChosen.value = false

    }

    fun optionSpeedLimitsChosen(){

        _speedLimitsChosen.value = true
        _type.value = 1

    }

    fun optionSpeedLimitsChosenComplete(){

        _speedLimitsChosen.value = false

    }

    fun optionOthersChosen(){

        _othersChosen.value = true
        _type.value = 2

    }

    fun optionOthersChosenComplete(){

        _othersChosen.value = false

    }

}