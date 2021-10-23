package com.example.android.navigation.screens.sign_options

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.*


/**
 * ViewModel containing all the logic needed to run the sign_options
 */
class SignOptionsViewModel(area : String, type : String, database: SignDatabaseDao, application: Application) : AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
    private var viewModelJob = Job()
    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
    */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
     */

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private val _sign =MutableLiveData<List<Signs>>()
    val sign: LiveData<List<Signs>>
        get() = _sign

    private val _step = MutableLiveData<List<Instructions>>()
    val step: LiveData<List<Instructions>>
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
            viewModelScope.launch {
                _sign.value = getSignsFromDatabase()
                //private val signs = database.filterGetSigns(area, type)

                // In Kotlin, the return@label syntax is used for specifying which function among
                // several nested ones this statement returns from.
                // In this case, we are specifying to return from launch(),
                // not the lambda.
                //val oldNight = tonight.value ?: return@launch

            }

    }

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */

    private suspend fun getSignsFromDatabase(): List<Signs>? {

        //return withContext(Dispatchers.IO) {

        return _database.value!!.getSign().value!!
        //}

    }

    fun getChosenSteps(){
        uiScope.launch {
            _step.value = getInsFromDatabase()
        }
    }

    private suspend fun getInsFromDatabase(): List<Instructions> {

        return _database.value!!.getIns(_signId.value!!).value!!
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