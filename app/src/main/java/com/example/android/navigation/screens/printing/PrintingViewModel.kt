package com.example.android.navigation.screens.printing

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.lifecycle.*
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import com.example.android.navigation.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import timber.log.Timber

import java.io.File




class PrintingViewModel (signId: Long, val database: SignDatabaseDao, application: Application, private val bluetoothAdapter : BluetoothAdapter, private val context : Context) : AndroidViewModel(application) {

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

    private val _isPrinting = MutableLiveData<Boolean>()
    val isPrinting: LiveData<Boolean>
        get() = _isPrinting

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    private val _progressProsent = MutableLiveData<String>()
    val progressProsent: LiveData<String>
        get() = _progressProsent

    private val _stepInTheWorks = MutableLiveData<String>()
    val stepInTheWorks: LiveData<String>
        get() = _stepInTheWorks

    private val _getData = MutableLiveData<Boolean>()
    val getData: LiveData<Boolean>
        get() = _getData

    private val _locationOnAxel = MutableLiveData<String>()
    val locationOnAxel: LiveData<String>
        get() = _locationOnAxel

    // Thread Variables

    private val _connected = MutableLiveData<Boolean>()
    val connected: LiveData<Boolean>
        get() = _connected

    private val _device = MutableLiveData<String>()
    val device: LiveData<String>
        get() = _device

    lateinit var mainThreadHandler : Handler
    lateinit var runnable: ThreadHandler
    lateinit var thread : Thread


    private val _receivedMessage = MutableLiveData<String>()
    val receivedMessage: LiveData<String>
        get() = _receivedMessage

    lateinit var sendMessage : String

    init {
        Timber.i("PrintingViewModel created.")

        _signId.value = signId
        getDataFromDatabase()
        _isPrinting.value = false
        _stepInTheWorks.value = ""
        _progress.value = 0
        _getData.value = false


        // Thread
        mainThreadHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    SEND -> {
                        //super.handleMessage(msg)
                        Timber.v("" + msg.obj)

                    }
                    RECEIVE -> {
                        Timber.v("" + msg.obj)
                        _receivedMessage.value = msg.obj.toString() //as LiveData<JsonArray>
                        update()
                    }
                    CONNECT -> {
                        Timber.v("" + msg.obj)

                        _connected.value = true
                        _device.value = msg.obj.toString()

                    }
                    DISCONNECT -> {
                        Timber.v("" + msg.obj)

                        _connected.value = false
                        _device.value = msg.obj.toString()

                    }
                }
            }
        }
        runnable = ThreadHandler(mainThreadHandler, context, bluetoothAdapter)
        thread = Thread(runnable)
        thread.start()
        Handler().postDelayed({
            connect()
        }, 1000)
        //connect()


    }

    fun connect() {
        val msg = Message()
        msg.what = CONNECT

        Timber.v("NULL  " + msg.what)
        runnable.workerThreadHandler!!.sendMessage(msg)


    }

    fun write(data : String){

        val msg = Message()
        msg.what = SEND
        msg.obj = data
        runnable.workerThreadHandler!!.sendMessage(msg)


    }

    fun shutDownTread() {
        val msg = Message()
        msg.what = QUIT_MSG

        Timber.v("NULL  " + msg.what)
        runnable.workerThreadHandler!!.sendMessage(msg)
    }


    //FUNCTIONS
    fun update(){

        val temp = _receivedMessage.value!!.split(",")

        Timber.v(""+temp)
        _progress.value = temp[3].toInt()
        _locationOnAxel.value = temp[2]+temp[0]+"moved: (x, y) "+temp[1]
        _progressProsent.value = temp[3]+"%"
        _stepInTheWorks.value = temp[2]+". "+temp[0]

    }


    private fun getDataFromDatabase() {
        uiScope.launch {

            _sign.value = getSignFromDatabase()
            _steps.value = getFromStepsDatabase()
            Timber.i("PrintingViewModel created.")
            _signName.value = _sign.value?.signName
            _signInfo.value = _sign.value?.info
            _signSource.value = _sign.value?.sourcePicture

            val imgFile = File(_signSource.value!!)

            if (imgFile.exists()) {
                _bitmap.value = BitmapFactory.decodeFile(imgFile.absolutePath)
            }
            _getData.value = true
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
        _isPrinting.value = true
    }

    fun energencyStop(){

        _isPrinting.value = false

    }




}