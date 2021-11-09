package com.example.android.navigation.screens.printing

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.navigation.MainActivity
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.R

import android.widget.ImageView

import android.graphics.BitmapFactory

import android.graphics.Bitmap

import java.io.File




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

    private val _steps = MutableLiveData<List<Instructions>>()
    val steps: LiveData<List<Instructions>>
        get() = _steps

    private val _sign = MutableLiveData<Signs>()
    val sign: LiveData<Signs>
        get() = _sign

    private var _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap>
        get() = _bitmap

    private val _signName = MutableLiveData<String>()
    val signName: LiveData<String>
        get() = _signName

    private val _signInfo = MutableLiveData<String>()
    val signInfo: LiveData<String>
        get() = _signInfo

    private val _signSource = MutableLiveData<String>()
    val signSource: LiveData<String>
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
            _signSource.value = "sign_images/${_sign.value?.sourcePicture}"

            val imgFile = File(_signSource.value)
            if (imgFile.exists()) {
                _bitmap = MutableLiveData(BitmapFactory.decodeFile(imgFile.absolutePath))
            }
        }
    }

    private suspend fun getStepsFromDatabase(): List<Instructions>? {

        return database.filterGetIns(_signId.value!!)

    }

    private suspend fun getSignFromDatabase(): Signs? {

        return database.filterGetSign(_signId.value!!)

    }

    fun startPrinting(){
    //TODO send info and steps from the list

    }

    fun energencyStop(){

        //TODO send stop command

    }




}