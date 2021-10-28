package com.example.android.navigation.screens.printing

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PrintingViewModel (signId: Long, val database: SignDatabaseDao, application: Application) : AndroidViewModel(application) {

    private val _signId = MutableLiveData<Long>()
    val signId: LiveData<Long>
        get() = _signId


    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    //TODO fix muoto
    private val _steps = MutableLiveData<List<Instructions>>()
    val steps: LiveData<List<Instructions>>
        get() = _steps

    private val _sign = MutableLiveData<Signs>()
    val sign: LiveData<Signs>
        get() = _sign

    private val _signName = MutableLiveData<String>()
    val signName: LiveData<String>
        get() = _signName

    private val _signInfo = MutableLiveData<String>()
    val signInfo: LiveData<String>
        get() = _signInfo

    private val _signSource = MutableLiveData<Int>()
    val signSource: LiveData<Int>
        get() = _signSource



    /*
       val nightsString = Transformations.map(_steps) { nights ->
           formatNights(nights, application.resources)
       }

       private val steps = database.filterGetIns(_singId.value!!)

        */

    init{
        Log.i("PrintingViewModel", "PrintingViewModel created.")


        _signId.value = signId
        initializeStep()
        initializeSign()


    }

    //FUNCTIONS
    fun initializeStep(){
        uiScope.launch {
            _steps.value = getStepsFromDatabase()
        }
    }

    fun initializeSign(){
        uiScope.launch {
            _sign.value = getSignFromDatabase()
            _signName.value = _sign.value?.signName
            _signInfo.value = _sign.value?.info
            _signSource.value = _sign.value?.sourcePicture
            //TODO signSource linking
        }
    }

    private suspend fun getStepsFromDatabase(): LiveData<List<Instructions>>? {

        return database.filterGetIns(_signId.value!!)

    }

    private suspend fun getSignFromDatabase(): LiveData<Signs>? {

        return database.filterGetSign(_signId.value!!)

    }

    fun startPrinting(){
    //TODO send info and steps from the list

    }
    fun energencyStop(){

        //TODO send stop command

    }




}