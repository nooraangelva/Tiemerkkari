package com.example.android.navigation.screens.imports

import android.app.Application
import androidx.core.app.ActivityCompat.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.*

import timber.log.Timber


class ImportViewModel (val database: SignDatabaseDao, application: Application) : ViewModel()  {

    private var viewModelJob = Job()

    override fun onCleared() {

        super.onCleared()
        viewModelJob.cancel()

    }

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)




    private val _stepList = MutableLiveData<ArrayList<Instructions>>()
    val stepList: LiveData<ArrayList<Instructions>>
        get() = _stepList

    private val _step = MutableLiveData<Instructions>()
    val step: LiveData<Instructions>
        get() = _step

    private val _sign = MutableLiveData<Signs>()
    val sign: LiveData<Signs>
        get() = _sign

    private val _signId = MutableLiveData<Long>()
    val signId: LiveData<Long>
        get() = _signId

    private var _futureId = MutableLiveData<Long>()
    val futureId: LiveData<Long>
        get() = _futureId

    private val _signName = MutableLiveData<String>()
    val signName: LiveData<String>
        get() = _signName

    private val _signInfo = MutableLiveData<String>()
    val signInfo: LiveData<String>
        get() = _signInfo

    private val _signSource = MutableLiveData<String>()
    val signSource: LiveData<String>
        get() = _signSource

    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

    private val _speedArea = MutableLiveData<Boolean>()
    val speedArea: LiveData<Boolean>
        get() = _speedArea


    init {
        Timber.i("PrintingViewModel created.")

        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            _futureId.value = getFutureSignIdFromDatabase()
            Timber.i("PrintingViewModel created."+_futureId.value)
        }
    }

    private suspend fun getFutureSignIdFromDatabase():  Long? {
        return withContext(Dispatchers.IO) {
            var temp: Long? = database.getBiggestSignId()
            temp ?: run {
                Timber.i("PrintingViewModel created.")
                temp = 0
            }
            temp
        }

    }

    fun createSign(){
        //TODO Check  if empty and linking
        /*
        _sign.value?.sourcePicture = "${_futureId.value}.PNG"
        _sign.value?.signName = _signName.value!!
        _sign.value?.info = _signInfo.value!!
        _sign.value?.speedArea = _speedArea.value!!
        _sign.value?.type = getType()
        */

        var temp = Signs()
        temp.sourcePicture = "${_futureId.value}.PNG"
        temp.signName = "Suora"
        temp.info = "iso"
        temp.speedArea = true
        temp.type = 1

        Timber.i("Import: "+_sign.value?.signName)
        Timber.i("Import: "+_sign.value?.sourcePicture)
        Timber.i("Import: "+_sign.value?.info)


        uiScope.launch {
            createSignToDatabase(temp)
            _signId.value = getSignIdFromDatabase(temp.signName)
            Timber.i("Import signId: "+_signId.value)
        }



    }

    private suspend fun getSignIdFromDatabase(temp: String): Long {
        return withContext(Dispatchers.IO) {
            var id = database.getSignId(temp)
            id ?: run {
                Timber.i("PrintingViewModel created.")
            }
            id
        }

    }

    private suspend fun createSignToDatabase(temp: Signs){

        withContext(Dispatchers.IO){

            database.insertSign(temp)
            //TODO oikein?

        }

    }

    //FUNCTIONS

    fun getType():Int{

        when (_type.value!!) {
            "Arrows" -> return 0
            "Others" -> return 2
            "SpeedLimits" -> return 1
            "Nuolet" -> return 0
            "Nopeusrajoitukset" -> return 1
            "Muut" -> return 2
            else -> {
                return 0
            }
        }

    }

    fun saveSteps(){

        //TODO final resting spot and maybe strat too?
        uiScope.launch {
            _stepList.value?.forEachIndexed { index, step ->

                _step.value = step
                _step.value?.step = index
                _step.value?.signId = _signId.value!!

                createStepToDatabase()

            }

        }

    }

    private fun createStepToDatabase():Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            database.insertIns(_step.value!!)
        }
        return true

    }

    fun delete(){
        uiScope.launch {
            deleteSignFromDatabase()
            deleteStepsFromDatabase()
        }

    }

    private fun deleteStepsFromDatabase():Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            database.clearIns(signId.value!!)
        }
        return true

    }

    private fun deleteSignFromDatabase():Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            database.clearSign(signId.value!!)
        }
        return true

    }

}