package com.example.android.navigation.screens.printing

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.lifecycle.*
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.android.navigation.*
import kotlinx.coroutines.*
import timber.log.Timber


// ViewModel containing all the logic needed to run the PrintingFragment
class PrintingViewModel (signId: Long, val database: SignDatabaseDao, application: Application, private val bluetoothAdapter : BluetoothAdapter, private val context : Context) : AndroidViewModel(application) {

    // DATABASE VARIABLES --------------------------------------------------------------------------

    // ViewModelJob allows us to cancel all coroutines started by this ViewModel.
    private var viewModelJob = Job()

    // Keeps track of all coroutines started by this ViewModel
    // All coroutines started in uiScope will launch in [Dispatchers.Main] which is
    // the main thread - because it updates the UI usually after
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    // Tells what sign was chosen in signOptionsFragment
    private val _signId = MutableLiveData<Long>()
    val signId: LiveData<Long>
        get() = _signId

    // RESULTS OF DATABASE GET ---------------------------------------------------------------------

    private val _steps = MutableLiveData<List<Instructions>>()

    private val _sign = MutableLiveData<Signs>()
    val sign: LiveData<Signs>
        get() = _sign

    private val _signName = MutableLiveData<String>()
    val signName: LiveData<String>
        get() = _signName

    private val _signInfo = MutableLiveData<String>()
    val signInfo: LiveData<String>
        get() = _signInfo

    private val _signSource = MutableLiveData<String>()
    val signSource: LiveData<String>
        get() = _signSource

    // PRINTING VARIABLES --------------------------------------------------------------------------

    // Is Arduino printing
    private val _isPrinting = MutableLiveData<Boolean>()
    val isPrinting: LiveData<Boolean>
        get() = _isPrinting

    // Is printing done
    private val _finished = MutableLiveData<Boolean>()
    val finished: LiveData<Boolean>
        get() = _finished

    // Stores the progress of the printing in Int (%)
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    // Stores the progress of the printing in String %
    private val _progressPercentage = MutableLiveData<String>()
    val progressPercentage: LiveData<String>
        get() = _progressPercentage

    // Tells which is the Arduino printing
    private val _stepInTheWorks = MutableLiveData<String>()
    val stepInTheWorks: LiveData<String>
        get() = _stepInTheWorks

    // Tells if the data has been received from the database
    private val _getData = MutableLiveData<Boolean>()
    val getData: LiveData<Boolean>
        get() = _getData

    // Tells where the printing device is at the moment on the axel
    private val _locationOnAxel = MutableLiveData<String>()
    val locationOnAxel: LiveData<String>
        get() = _locationOnAxel

    // THREAD VARIABLES ----------------------------------------------------------------------------

    // Tells if Arduino is connected
    private val _connected = MutableLiveData<Boolean>()
    val connected: LiveData<Boolean>
        get() = _connected

    // Stores devices name
    private val _device = MutableLiveData<String>()
    val device: LiveData<String>
        get() = _device

    // stores both threads (main and "connection")
    private var mainThreadHandler : Handler
    var runnable: ThreadHandler
    var thread : Thread

    // Stores the received message that was gotten from Arduino
    private val _receivedMessage = MutableLiveData<String>()

    // Initialized values, functions and events when viewModel is created
    init {

        // Initialized values
        _signId.value = signId
        getDataFromDatabase()
        _isPrinting.value = false
        _stepInTheWorks.value = ""
        _progress.value = 0
        _getData.value = false
        _finished.value = false


        // Creating mainThread
        // handles incoming messages from the other thread
        mainThreadHandler = object : Handler(Looper.getMainLooper()) {

            override fun handleMessage(msg: Message) {

                when (msg.what) {

                    // Receiving progress info from Arduino
                    RECEIVE -> {

                        _receivedMessage.value = msg.obj.toString()
                        update()

                    }
                    // Tells that the connection has been established with Arduino
                    CONNECT -> {

                        _connected.value = true
                        _device.value = msg.obj.toString()

                    }
                    // Disconnects from Arduino and quits thread
                    DISCONNECT -> {

                        _connected.value = false
                        _device.value = msg.obj.toString()

                    }

                }

            }

        }

        runnable = ThreadHandler(mainThreadHandler, context, bluetoothAdapter)
        thread = Thread(runnable)
        thread.start()
        connect()

    }

    // THREAD/CONNECTION ---------------------------------------------------------------------------

    // Tells the devise to connect with Arduino using BLE GATT,
    // forming of the connection happens in the other thread
    private fun connect() {

        val msg = Message()
        msg.what = CONNECT
        runnable.workerThreadHandler!!.sendMessage(msg)

    }

    // Sends a message with the wanted steps to Arduino via the other thread
    private fun write(data : String){

        val msg = Message()
        msg.what = SEND
        msg.obj = data
        runnable.workerThreadHandler!!.sendMessage(msg)

    }

    // Shutdowns the Thread if needed to
    fun shutDownTread() {
        val msg = Message()
        msg.what = QUIT_MSG

        Timber.v("NULL  " + msg.what)
        runnable.workerThreadHandler!!.sendMessage(msg)
    }

    // updates Arduino's printing status to the user when new status is received
    fun update(){

        //Temp = Command, x.y progress, step, %,
        val temp = _receivedMessage.value!!.split(",")
        val mes = temp[1].split(".")

        // Sets the values for the user
        _progress.value = temp[3].toInt()
        _locationOnAxel.value = temp[2]+". "+temp[0]+" Moved: (x, y): "+mes[0]+"cm "+mes[1]+" cm"
        _progressPercentage.value = temp[3]+"%"
        _stepInTheWorks.value = _steps.value?.get(temp[2].toInt())?.step.toString()+
                ". "+temp[0]+"to move: (x,y): "+_steps.value?.get(temp[2].toInt())?.parX+" cm"+
                _steps.value?.get(temp[2].toInt())?.parY+" cm"

        // TODO CHECK IF FINISHED printing
        /*if(){
            _finished.value = true
        }*/

    }

    // Sets the printing status, so that the user can see that it is happening
    fun startPrinting(){

        _isPrinting.value = true
        _steps.value?.forEachIndexed { index, step ->

            val array = """{"Commands":["${step.order}","${step.parY}","${step.parX}","${step.paint}","${step.step}" ]}"""

            write(array)

        }

    }

    // Sets the printing status, so that the user can see that it is stopped
    fun emergencyStop(){

        _isPrinting.value = false
        val array = """{"Commands":["STOP"]}"""
        write(array)

    }

    // DATABASE ------------------------------------------------------------------------------------

    // Gets wanted info about the chosen sign, steps to send
    // to Arduino and showcases signs data to user
    private fun getDataFromDatabase() {

        uiScope.launch {

            _sign.value = getSignFromDatabase()
            _steps.value = getFromStepsDatabase()
            _signName.value = _sign.value?.signName
            _signInfo.value = _sign.value?.info
            _signSource.value = _sign.value?.sourcePicture

            // tells fragment that it has the data finally,
            // so that it can set the picture for the sign
            _getData.value = true

        }

    }

    // Request to database: gets signs steps,
    // uses Coroutine so the user doesn't feel the delay
    private suspend fun getFromStepsDatabase() : List<Instructions>{

        return withContext(Dispatchers.IO) {

            val temp = database.filterGetIns(_signId.value!!)
            temp

        }

    }

    // Request to database: gets sign,
    // uses Coroutine so the user doesn't feel the delay
    private suspend fun getSignFromDatabase(): Signs{

        return withContext(Dispatchers.IO) {

            val temp = database.filterGetSign(_signId.value!!)

            temp

        }

    }

    // Clears Coroutines
    override fun onCleared() {

        super.onCleared()
        viewModelJob.cancel()

    }

}