package com.example.android.navigation.screens.imports

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.core.app.ActivityCompat.*
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.navigation.Step
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.*

import timber.log.Timber


class ImportViewModel (val database: SignDatabaseDao, application: Application) : ViewModel() {

    private var viewModelJob = Job()

    override fun onCleared() {

        super.onCleared()
        viewModelJob.cancel()

    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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

    private var _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var _signCreated = MutableLiveData<Boolean>()
    val signCreated: LiveData<Boolean>
        get() = _signCreated

    // Inputs

    val signName = MutableLiveData<String>()

    val signInfo = MutableLiveData<String>()

    val signSource = MutableLiveData<String>()

    val type = MutableLiveData<String>()

    val speedArea = MutableLiveData<Boolean>()





    init {
        Timber.i("PrintingViewModel created.")

        _signCreated.value = false
        speedArea.value = false
    }

    fun savedImagepath(temp: String){

        signSource.value = temp
    }



    fun createSign() {

        val temp = Signs()
        temp.sourcePicture = signSource.value!!
        temp.signName = signName.value!!
        temp.info = signInfo.value!!
        temp.speedArea = speedArea.value!!
        temp.type = getType()

        Timber.i("Import: " + temp.signName)
        Timber.i("Import: " + temp.info)
        Timber.i("Import: " + temp.speedArea)
        Timber.i("Import: " + temp.type)

        if(temp.sourcePicture.isEmpty()){
            _error.value = "Get Picture"
        }
        else if(temp.signName.isEmpty()){
            _error.value = "Set Name"
        }
        else if(temp.info.isEmpty()){
            _error.value = "set Info"
        }
        else if(temp.type == -1){
            _error.value = "set Type"
        }
        else{

            uiScope.launch {
                createSignToDatabase(temp)
                _signId.value = getSignIdFromDatabase(temp.signName)
                Timber.i("Import signId: " + _signId.value)
                _signCreated.value = true
            }

        }

    }

    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            type.value = parent?.getItemAtPosition(position) as String
        }
    }

    private suspend fun getSignIdFromDatabase(temp: String): Long {
        return withContext(Dispatchers.IO) {
            val id = database.getSignId(temp)
            id ?: run {
                Timber.i("PrintingViewModel created.")
            }
            id
        }

    }

    private suspend fun createSignToDatabase(temp: Signs) {

        withContext(Dispatchers.IO) {

            database.insertSign(temp)
            //TODO oikein?

        }

    }

    //FUNCTIONS

    fun getType(): Int {

        when (type.value!!) {
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

    fun saveSteps(step: Step, index: Int) {

        uiScope.launch {

            val temp : Instructions = Instructions(0,_signId.value!!,
                index,whichOrder(step.order),step.parX.toInt(),
                step.parY.toInt(),whichPaint(step.paint))


            Timber.i("Import O: " + whichOrder(step.order))
            Timber.i("Import P: " + whichPaint(temp.paint).toString())
            Timber.i("Import: X" + temp.parY.toString())
            Timber.i("Import: Y" + temp.parX.toString())

            if(step.parX.isEmpty()){
                _error.value = "Set value to X"
            }
            else if(step.parY.isEmpty()){
                _error.value = "Set value to Y"
            }
            else{
                createStepToDatabase(temp)
            }

        }

    }

    private fun whichOrder(tmp : Int): String{
        when (tmp) {
            1 -> return "Vertical"
            2 -> return "Horizontal"
            3 -> return "Arc"
            4 -> return "Diagonal"
            else -> {
                return ""
            }
        }
    }

    private fun whichPaint(tmp : Int): Int{
        when (tmp) {
            5 -> return 0
            6 -> return 1
            7 -> return 2
            8 -> return 3
            else -> {
                return 0
            }
        }
    }

    private suspend fun createStepToDatabase(tmp : Instructions){
        CoroutineScope(Dispatchers.IO).launch {
            database.insertIns(tmp)
        }
    }

    fun delete(){
        uiScope.launch {
            deleteSignFromDatabase()
            deleteStepsFromDatabase()
            _signCreated.value = false
        }

    }

    private suspend fun deleteStepsFromDatabase():Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            database.clearIns(signId.value!!)
        }
        return true

    }

    private suspend fun deleteSignFromDatabase():Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            database.clearSign(signId.value!!)
        }
        return true

    }

}