package com.example.android.navigation.screens.sign_options

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import com.example.android.navigation.database.*
import kotlinx.coroutines.*
import timber.log.Timber


/**
 * ViewModel containing all the logic needed to run the sign_options
 */
class SignOptionsViewModel(area : Boolean, type : Int, val database: SignDatabaseDao, application: Application) : AndroidViewModel(application) {

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


    init {
        Timber.i("SignOptionsViewModel created.")
        _area.value = area
        _type.value = type
        _eventSubmit.value = false

        initializeTonight()

    }

    //FUNCTIONS
    private fun initializeTonight() {
        uiScope.launch {
            _sign.value = getSignsFromDatabase()
            Timber.i("PrintingViewModel created.")
            Timber.i("Number of signs: "+_sign.value!!.size)
        }
    }


    private suspend fun getSignsFromDatabase():  List<Signs> {
        return withContext(Dispatchers.IO) {
            //TODO vaihda takas ja tarkista toiminta
            //val temp: List<Signs> = database.filterGetSigns(_type.value!!,_area.value!!)
            val temp: List<Signs> = database.getSign()
            temp
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