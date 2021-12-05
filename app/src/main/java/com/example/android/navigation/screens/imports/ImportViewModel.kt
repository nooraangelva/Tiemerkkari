package com.example.android.navigation.screens.imports

import android.app.Application
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.navigation.dataForm.Step
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.*
import timber.log.Timber

// ViewModel containing all the logic needed to run the ImportFragment
class ImportViewModel (val database: SignDatabaseDao, application: Application) : ViewModel() {

// DATABASE VARIABLES --------------------------------------------------------------------------

    // ViewModelJob allows us to cancel all coroutines started by this ViewModel.
    private var viewModelJob = Job()

    // Keeps track of all coroutines started by this ViewModel
    // All coroutines started in uiScope will launch in [Dispatchers.Main] which is
    // the main thread - because it updates the UI usually after
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Stores a step in proper form for step creation
    private val _step = MutableLiveData<Instructions>()
    val step: LiveData<Instructions>
        get() = _step

    // Stores sign data from input for sign creation
    private val _sign = MutableLiveData<Signs>()
    val sign: LiveData<Signs>
        get() = _sign

    // Stores the created signs signId for steps to use
    private val _signId = MutableLiveData<Long>()
    val signId: LiveData<Long>
        get() = _signId

    // Tells the error to user if one occurs when creating sign or the steps
    private var _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    // Tells if the sign has been created
    private var _signCreated = MutableLiveData<Boolean>()
    val signCreated: LiveData<Boolean>
        get() = _signCreated

    // INPUTS FROM FRAGMENT ------------------------------------------------------------------------

    val signName = MutableLiveData<String>()

    val signInfo = MutableLiveData<String>()

    private val signSource = MutableLiveData<String>()

    val type = MutableLiveData<String>()

    val speedArea = MutableLiveData<Boolean>()

    // Initialized values, functions and events when viewModel is created
    init {

        // Initialized values
        _signCreated.value = false
        speedArea.value = false

    }

    // DATABASE ------------------------------------------------------------------------------------

    // Gets the saved images path for viewModel
    fun savedImagePath(temp: String){

        signSource.value = temp

    }

    // Creates signs from input data
    fun createSign() {

        val temp = Signs()
        temp.sourcePicture = signSource.value!!
        temp.signName = signName.value!!
        temp.info = signInfo.value!!
        temp.speedArea = speedArea.value!!
        temp.type = getType()

        // Checks that the input values are not empty
        when {
            temp.sourcePicture.isEmpty() -> {
                _error.value = "Get Picture"
            }
            temp.signName.isEmpty() -> {
                _error.value = "Set Name"
            }
            temp.info.isEmpty() -> {
                _error.value = "set Info"
            }
            temp.type == -1 -> {
                _error.value = "set Type"
            }
            else -> {

                // Sends sign input to be put to the database and gets the created signs singId,
                // tells the user has been created
                uiScope.launch {

                    createSignToDatabase(temp)
                    _signId.value = getSignIdFromDatabase(temp.signName)
                    _signCreated.value = true

                }

            }

        }

    }

    // Request to database: gets signs ID,
    // uses Coroutine so the user doesn't feel the delay
    private suspend fun getSignIdFromDatabase(temp: String): Long {

        return withContext(Dispatchers.IO) {

            val id = database.getSignId(temp)

            id

        }

    }

    // Request to database: creates sign,
    // uses Coroutine so the user doesn't feel the delay
    private suspend fun createSignToDatabase(temp: Signs) {

        withContext(Dispatchers.IO) {

            database.insertSign(temp)

        }

    }

    // Creates steps from input data
    fun saveSteps(step: Step, index: Int) {

        uiScope.launch {

            val temp = Instructions(0,_signId.value!!,
                index,whichOrder(step.order),step.parX.toInt(),
                step.parY.toInt(),whichPaint(step.paint))

            when {

                step.parX.isEmpty() -> {
                    _error.value = "Set value to X"
                }
                step.parY.isEmpty() -> {
                    _error.value = "Set value to Y"
                }
                else -> {
                    createStepToDatabase(temp)
                }

            }

        }

    }

    // Request to database: creates step,
    // uses Coroutine so the user doesn't feel the delay
    private suspend fun createStepToDatabase(tmp : Instructions){

        CoroutineScope(Dispatchers.IO).launch {

            database.insertIns(tmp)

        }

    }

    // Deletes created data
    fun delete(){

        uiScope.launch {

            deleteSignFromDatabase()
            deleteStepsFromDatabase()
            _signCreated.value = false

        }

    }

    // Request to database: deletes steps,
    // uses Coroutine so the user doesn't feel the delay
    private suspend fun deleteStepsFromDatabase():Boolean {

        CoroutineScope(Dispatchers.IO).launch {

            database.clearIns(signId.value!!)

        }

        return true

    }

    // Request to database: deletes signs,
    // uses Coroutine so the user doesn't feel the delay
    private suspend fun deleteSignFromDatabase():Boolean {

        CoroutineScope(Dispatchers.IO).launch {

            database.clearSign(signId.value!!)

        }

        return true

    }

    // Clears Coroutines
    override fun onCleared() {

        super.onCleared()
        viewModelJob.cancel()

    }

    // GETS SELECTED CHOICE ------------------------------------------------------------------------

    // Checks what type was chosen
    private fun getType(): Int {

        return when (type.value!!) {

            "Arrows" -> 0
            "Others" -> 2
            "SpeedLimits" -> 1
            "Nuolet" -> 0
            "Nopeusrajoitukset" -> 1
            "Muut" -> 2
            else -> {
                0
            }

        }

    }

    // Click listener for type selection from spinner
    val clicksListener = object : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            type.value = parent?.getItemAtPosition(position) as String

        }

    }

    // Checks what order was chosen
    private fun whichOrder(tmp : Int): String{

        return when (tmp) {

            1 -> "Vertical"
            2 -> "Horizontal"
            3 -> "Arc"
            4 -> "Diagonal"
            else -> {
                ""
            }

        }

    }

    // Checks what paint was chosen
    private fun whichPaint(tmp : Int): Int{

        return when (tmp) {

            5 -> 0
            6 -> 1
            7 -> 2
            8 -> 3
            else -> {
                0
            }

        }

    }

}