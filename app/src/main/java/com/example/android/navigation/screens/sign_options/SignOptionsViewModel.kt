package com.example.android.navigation.screens.sign_options

import android.app.Application
import androidx.lifecycle.*
import com.example.android.navigation.database.*
import kotlinx.coroutines.*
import timber.log.Timber



// ViewModel containing all the logic needed to run the sign_options
class SignOptionsViewModel(area : Boolean, type : Int, val database: SignDatabaseDao, application: Application) : AndroidViewModel(application) {

    // Database variables

    // ViewModelJob allows us to cancel all coroutines started by this ViewModel
    private var viewModelJob = Job()

    // Keeps track of all coroutines started by this ViewModel
    // All coroutines started in uiScope will launch in [Dispatchers.Main] which is
    // the main thread - because it updates the UI usually after
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    // Stores the list of Signs gotten from the database
    private val _sign =MutableLiveData<List<Signs>>()
    val sign: LiveData<List<Signs>>
        get() = _sign

    // Stores parameter for printing fragment about the chosen sign
    private val _signId = MutableLiveData<Long>()
    val signId: LiveData<Long>
        get() = _signId

    // Used to filter signs with type from database GET
    private val _type = MutableLiveData<Int>()

    // Used to filter signs with speed area from database GET
    private val _area = MutableLiveData<Boolean>()

    init {

        Timber.i("SignOptionsViewModel created.")
        _area.value = area
        _type.value = type

        signsFromDatabase()

    }

    // DATABASE ------------------------------------------------------------------------------------

    // Calls the suspend function to retrieve the data for _sign
    private fun signsFromDatabase() {

        uiScope.launch {

            _sign.value = getSignsFromDatabase()

        }

    }

    // Coroutine to retrieve signs from database
    private suspend fun getSignsFromDatabase():  List<Signs> {

        return withContext(Dispatchers.IO) {

            val temp: List<Signs> = database.filterGetSigns(_type.value!!,_area.value!!)

            temp

        }

    }

    // Cancels all the coroutines
    override fun onCleared() {

        super.onCleared()
        viewModelJob.cancel()

    }

}