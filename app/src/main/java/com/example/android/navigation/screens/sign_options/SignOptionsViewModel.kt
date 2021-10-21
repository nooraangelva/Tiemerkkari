package com.example.android.navigation.screens.sign_options

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import com.example.android.navigation.formatSigns
import kotlinx.coroutines.*


/**
 * ViewModel containing all the logic needed to run the sign_options
 */
class SignOptionsViewModel(area : String, type : String, database: SignDatabaseDao, application: Application) : ViewModel() {


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private val _sign = MutableLiveData<Signs?>()
    val sign: LiveData<Signs?>
        get() = _sign

    private val _step = MutableLiveData<Instructions?>()
    val step: LiveData<Instructions?>
        get() = _step



    private val _application = MutableLiveData<Application>()
    val application: LiveData<Application>
        get() = _application

    private val _database = MutableLiveData<SignDatabaseDao?>()
    val database: LiveData<SignDatabaseDao?>
        get() = _database

    private val _signId = MutableLiveData<Long>()
    val signId: LiveData<Long>
        get() = _signId




//TODO Area and type to Boolean and initialize signId

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
        _database.value = database
        _application.value = application

        initializeSign()

    }

    //FUNCTIONS

    private fun initializeSign(){
        uiScope.launch {
            _sign.value = getSignsFromDatabase()
            //private val signs = database.filterGetSigns(area, type)

            // In Kotlin, the return@label syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch(),
            // not the lambda.
            //val oldNight = tonight.value ?: return@launch

            val signsString = Transformations.map(signs) { signs ->
                formatSigns(signs, _application.value!!.resources)
            }
        }
    }

    private suspend fun getSignsFromDatabase(): LiveData<List<Signs>> {
        return withContext(Dispatchers.IO) {
            var sign = _database.value!!.getSign()
            sign
        }
    }

    fun getChosenSteps(){
        uiScope.launch {
            _step.value = getInsFromDatabase()
        }
    }

    private suspend fun getInsFromDatabase(): LiveData<List<Instructions>> {
        return withContext(Dispatchers.IO) {
            var step= _database.value!!.filterGetIns(_signId.value!!)
            step
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun eventSubmit(){

        _eventSubmit.value = true

    }

    fun submitComplete(){

        _eventSubmit.value = false
    }



}