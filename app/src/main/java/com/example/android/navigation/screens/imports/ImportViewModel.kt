package com.example.android.navigation.screens.imports

import android.app.Application
import androidx.core.app.ActivityCompat.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import timber.log.Timber


class ImportViewModel (val database: SignDatabaseDao, application: Application) : ViewModel()  {

    private var viewModelJob = Job()
    private val GET_IMAGE = 100


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


    }

    //FUNCTIONS

    fun createSign(){
        _signSource.value = "${_futureId.value}.JPEG"
        createSignToDatabase()

    }

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

    private fun createSignToDatabase(): Boolean {

        CoroutineScope(Dispatchers.IO).launch {

            _sign.value?.sourcePicture = _signSource.value!!
            _sign.value?.signName = _signName.value!!
            _sign.value?.info = _signInfo.value!!
            _sign.value?.speedArea = _speedArea.value!!
            _sign.value?.type = getType()

            database.insertSign(_sign.value!!)
            //TODO oikein?
            _signId.value = database.getSignId(_signName.value!!)
        }
        return true

    }

    private fun getSignIdFromDatabase(): Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            _signId.value = database.getSignId(_signName.value!!)
        }
        return true

    }

    private suspend fun getFutureSignIdFromDatabase(): Long? {
        //CoroutineScope(Dispatchers.IO).launch {

            return database.getBiggestSignId()
        //}

    }

/*    fun newStep(){
        _stepList.value!!.add(Instructions())
    }
*/
    fun saveSteps(){

        //TODO final resting spot and maybe strat too?

        _stepList.value?.forEachIndexed { index, step ->


            _step.value = step
            _step.value?.step = index
            _step.value?.signId = _signId.value!!

            createStepToDatabase()

        }

    }

    private fun createStepToDatabase():Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            database.insertIns(_step.value!!)
        }
        return true

    }

    fun delete(){

        deleteSignFromDatabase()
        deleteStepsFromDatabase()

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

    // DOWNLOADING IMAGE

    fun imageDownload(){

        uiScope.launch {
            _futureId = MutableLiveData(getFutureSignIdFromDatabase())

        }
    }





}