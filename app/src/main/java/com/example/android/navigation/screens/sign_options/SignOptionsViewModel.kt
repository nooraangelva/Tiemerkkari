package com.example.android.navigation.screens.sign_options

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import com.example.android.navigation.database.*
import kotlinx.coroutines.*


/**
 * ViewModel containing all the logic needed to run the sign_options
 */
class SignOptionsViewModel(area : Boolean, type : Int, database: SignDatabaseDao, application: SignApplication) : AndroidViewModel(application) {

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



    var signs : LiveData<List<Signs>>
    private var viewModelJob = Job()

    //val repository = repository

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

    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    private val _type = MutableLiveData<Int>()
    val type: LiveData<Int>
        get() = _type
    private val _area = MutableLiveData<Boolean>()
    val area: LiveData<Boolean>
        get() = _area

    //lateinit var signs :LiveData<List<Signs>>

    init{
        Log.i("SignOptionsViewModel", "SignOptionsViewModel created.")
        _area.value = area
        _type.value = type
        _eventSubmit.value = false
        _database.value = database
        _application.value = application

        val db = application.database
        val repository: SignRepository = application.repository
        signs = repository.getSignsFromDatabase(_type.value!!, _area.value!!).asLiveData()

        //initializeSign()

    }

    //FUNCTIONS


    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */


    fun eventSubmit(){

        _eventSubmit.value = true

    }

    fun submitComplete(){

        _eventSubmit.value = false
    }



}