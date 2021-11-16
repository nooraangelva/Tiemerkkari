package com.example.android.navigation.screens.printing

import android.app.Application
import androidx.lifecycle.*
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import kotlinx.coroutines.*
import timber.log.Timber

import java.io.File




class PrintingViewModel (signId: Long, val database: SignDatabaseDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)


    private val _signId = MutableLiveData<Long>()
    val signId: LiveData<Long>
        get() = _signId

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

    init {
        Timber.i("PrintingViewModel created.")

        _signId.value = signId
        getDataFromDatabase()


    }

    //FUNCTIONS
    private fun getDataFromDatabase() {
        uiScope.launch {

            _sign.value = getSignFromDatabase()
            _steps.value = getFromStepsDatabase()
            Timber.i("PrintingViewModel created.")
            _signName.value = _sign.value?.signName
            _signInfo.value = _sign.value?.info
            _signSource.value = "sign_images/${_sign.value?.sourcePicture}"

            val imgFile = File(_signSource.value!!)
            if (imgFile.exists()) {
                _bitmap = MutableLiveData(BitmapFactory.decodeFile(imgFile.absolutePath))
            }

        }
    }

    private suspend fun getFromStepsDatabase() : List<Instructions>{
        return withContext(Dispatchers.IO) {

            val temp = database.filterGetIns(_signId.value!!)
            temp
        }

    }

    private suspend fun getSignFromDatabase(): Signs{
        return withContext(Dispatchers.IO) {

            val temp = database.filterGetSign(_signId.value!!)
            temp
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun startPrinting(){
    //TODO send info and steps from the list

    }

    fun energencyStop(){

        //TODO send stop command

    }




}