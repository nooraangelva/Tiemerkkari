package com.example.android.navigation.screens.imports

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.*
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.navigation.R
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import com.google.android.material.internal.ContextUtils.getActivity
import androidx.core.app.ActivityCompat.startActivityForResult

import android.os.Build
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.database.Cursor


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

    private val _signName = MutableLiveData<String>()
    val signName: LiveData<String>
        get() = _signName

    private val _signInfo = MutableLiveData<String>()
    val signInfo: LiveData<String>
        get() = _signInfo

    private val _signSource = MutableLiveData<Int>()
    val signSource: LiveData<Int>
        get() = _signSource

    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

    private val _speedArea = MutableLiveData<Boolean>()
    val speedArea: LiveData<Boolean>
        get() = _speedArea

    /*

       private val steps = database.filterGetIns(_singId.value!!)



    val nightsString = Transformations.map(steps) { nights ->
        formatNights(nights, application.resources)
    }*/

    init{
        Log.i("PrintingViewModel", "PrintingViewModel created.")


    }

    //FUNCTIONS

    fun createSign(){

        uiScope.launch {
            createSignToDatabase()
        }

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

    private suspend fun createSignToDatabase(): Boolean {

        _sign.value?.sourcePicture = _signSource.value!!
        _sign.value?.signName = _signName.value!!
        _sign.value?.info = _signInfo.value!!
        _sign.value?.speedArea = _speedArea.value!!
        _sign.value?.type = getType()

        database.insertSign(_sign.value!!)
        _signId.value = database.getSignId(_signName.value!!)
        return true

    }
/*
    private suspend fun getSignIdFromDatabase(): Boolean {

        _signId.value = database.getSignId(_signName.value!!)
        return true

    }
*/

    fun newStep(){
        _stepList.value!!.add(Instructions())
    }

    fun saveSteps(){

        _stepList.value?.forEachIndexed { index, step ->

            Log.d("<RESULT>", " ${step.step} - ${step.order}")
            _step.value = step
            _step.value?.step = index
            _step.value?.signId = _signId.value!!

            uiScope.launch {

                createStepToDatabase()
            }
        }

    }

    private suspend fun createStepToDatabase():Boolean {

        database.insertIns(_step.value!!)
        return true

    }

    fun delete(){

        uiScope.launch {
            deleteSignFromDatabase()
            deleteStepsFromDatabase()
        }

    }

    private suspend fun deleteStepsFromDatabase():Boolean {

        database.clearIns(signId.value!!)
        return true

    }

    private suspend fun deleteSignFromDatabase():Boolean {

        database.clearSign(signId.value!!)
        return true

    }

    // DOWNLOADING IMAGE

    fun imageDownload(){




    }





}