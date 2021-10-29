package com.example.android.navigation.screens.imports

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
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

class ImportViewModel (val database: SignDatabaseDao, application: Application) : ViewModel()  {

    val REQUEST_CODE = 100

    private var viewModelJob = Job()

    override fun onCleared() {

        super.onCleared()
        viewModelJob.cancel()

    }

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private val _steps = MutableLiveData<List<Instructions>>()
    val steps: LiveData<List<Instructions>>
        get() = _steps

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

    /*

       private val steps = database.filterGetIns(_singId.value!!)

        */

    init{
        Log.i("PrintingViewModel", "PrintingViewModel created.")


    }

    //FUNCTIONS

    fun initializeStep(){

        uiScope.launch {
            createStepsToDatabase()
            getStepsFromDatabase()
        }

    }

    fun initializeSign(){

        uiScope.launch {
            createSignToDatabase()
        }

    }

    fun saveSteps(){

        uiScope.launch {
            UpdateStepsToDatabase()
        }

    }

    fun delete(){

        uiScope.launch {
            deleteSignfromDatabase()
            deleteStepsfromDatabase()
        }

    }


    private suspend fun createStepsToDatabase():Boolean {

        database.insertIns(_step.value!!)
        return true

    }

    private suspend fun UpdateStepsToDatabase():Boolean {

        database.insertIns(_step.value!!)
        return true

    }

    private suspend fun deleteStepsfromDatabase():Boolean {

        database.clearIns(signId.value!!)
        return true

    }

    private suspend fun deleteSignfromDatabase():Boolean {

        database.clearSign(signId.value!!)
        return true

    }

    private suspend fun getStepsFromDatabase(): Boolean {

        _steps.value = database.filterGetIns(_signId.value!!)
        return true

    }

    private suspend fun createSignToDatabase(): Boolean {

        database.insertSign(_sign.value!!)
        return true

    }

    // DOWNLOADING IMAGE

    fun imageDownload(){

        openGalleryForImage()

    }

    private fun openGalleryForImage() {
        /*
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"*/

        startActivityForResult(intent, REQUEST_CODE)*/
    }
    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            //printingImageView.setImageURI(data?.data) // handle chosen image
        }

    }*/


}