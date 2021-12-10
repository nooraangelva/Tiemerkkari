package com.example.android.navigation

import android.bluetooth.*
import android.bluetooth.BluetoothDevice.TRANSPORT_LE
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import timber.log.Timber

class ThreadHandler(val mainThreadHandler: Handler?, val thisContext : Context, val bluetoothAdapter: BluetoothAdapter) : Runnable{

    // BLE and threads loop handler variables
    // (so every function can access them easily)
    var workerThreadHandler : Handler? = null
    var bluetoothGatt : BluetoothGatt? = null
    lateinit var bluetoothDevice : BluetoothDevice

    // Contains Loop where messages are handled,
    // is initialized with start() in the main thread
    override fun run() {

        Looper.prepare()
        Timber.tag("ThreadHandler").v("start()")

        workerThreadHandler = object : Handler(Looper.myLooper()!!) {

            // Handles and receives messages
            override fun handleMessage(msg: Message) {

                // Checks what kind of message has been received
                // acts accordingly to that
                when (msg.what) {

                    // Sends steps or stop message to Arduino in a string format
                    SEND -> {
                        Timber.tag("ThreadHandler").v("SEND")

                        // Gets the address and channel where to send
                        val service : BluetoothGattService? = bluetoothGatt?.getService(SERVICE_UUID)
                        val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID_SEND)

                        // Data to be send in a string format
                        val data = msg.obj

                        workerThreadHandler?.postDelayed({
                            // do something after 1000ms
                            characteristic!!.setValue(data.toString())

                            // Sends the data and also sets a notification
                            // so Arduino knows there is new data
                            bluetoothGatt?.writeCharacteristic(characteristic)
                            bluetoothGatt?.setCharacteristicNotification(characteristic,true)
                        }, 100)


                        val characteristic1 = service?.getCharacteristic(CHARACTERISTIC_UUID_RECEIVE)
                        bluetoothGatt?.setCharacteristicNotification(characteristic1, true)
                        Timber.tag("ThreadHandler").v(""+characteristic?.value)



                        // Sends a response to MainThread that
                        // the data has been send and printing will start or stop
                        val msgReply = Message()
                        msgReply.what = SEND
                        if(data.toString().contains("STOP",true)){

                            msgReply.obj = "Printing stopped."

                        } else {

                            msgReply.obj = "Printing started."

                        }

                        mainThreadHandler!!.sendMessage(msgReply)

                    }
                    // Starts scanning for the Arduino device and if it finds it,
                    // they will form a connection
                    CONNECT -> {

                        Timber.tag("ThreadHandler").v("CONNECT")
                        // Filters don't work
                        //var filter = ScanFilter.Builder().setDeviceName("TONIBLE").build()
                        //var filters: MutableList<ScanFilter> = mutableListOf(filter)
                        //var setting = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
                        //bluetoothAdapter?.bluetoothLeScanner?.startScan(filters, setting, bleScanCallback)
                        bluetoothAdapter.bluetoothLeScanner?.startScan(bleScanCallback)

                    }
                    // Closes BLE Gatt and the thread
                    QUIT_MSG -> {

                        Timber.tag("ThreadHandler").v("QUIT_MSG")

                        bluetoothGatt?.close()
                        Looper.myLooper()?.quit()

                    }

                }

            }

        }

        Looper.loop()

    }

    // Connects to the found device and stops scanning for it as it is now unnecessary,
    // sends also a reply to mainThread that the connection has been formed with the device
    fun connectToDevice(bluetoothDevice: BluetoothDevice, name : String) {

        bluetoothGatt = bluetoothDevice.connectGatt( thisContext, true, bleGattCallback)
        val msgReply = Message()
        msgReply.what = CONNECT
        msgReply.obj = name
        mainThreadHandler!!.sendMessage(msgReply)

        bluetoothAdapter.bluetoothLeScanner?.stopScan(bleScanCallback)

        Timber.tag("ThreadHandler").v(bluetoothDevice.name)

    }

    // Callback that scans to find the device with a specific name,
    // when or if it finds it, it call the connectToDevice() function
    private val bleScanCallback: ScanCallback by lazy {

        object : ScanCallback() {

            override fun onScanResult(callbackType: Int, result: ScanResult) {

                super.onScanResult(callbackType, result)
                bluetoothDevice = result.device

                if(!bluetoothDevice.name.isNullOrEmpty()) {

                    if (bluetoothDevice.name.contains("Testi", true)) {

                        Timber.tag("ThreadHandler").v("Device found: ${bluetoothDevice.name}")
                        connectToDevice(bluetoothDevice, bluetoothDevice.name)

                    }

                }

            }

        }

    }

    // Handles BLE Gatt connection changes:
    // Connection state and if this device receives messages from Arduino
    private val bleGattCallback: BluetoothGattCallback by lazy {

        object : BluetoothGattCallback() {

            // If Connection state changes
            override fun onConnectionStateChange(
                gatt: BluetoothGatt?,
                status: Int,
                newState: Int
            ) {

                super.onConnectionStateChange(gatt, status, newState)

                // when they are connected,
                // message will be sen to MainThread about the status change
                if (newState == BluetoothProfile.STATE_CONNECTED) {

                    Timber.tag("ThreadHandler").v("STATE_CONNECTED")
                    bluetoothGatt?.discoverServices()

                }
                // When the devices disconnect,
                // message will be sen to MainThread about the status change
                // and scanning will resume again
                else{

                    Timber.tag("ThreadHandler").v("STATE_DISCONNECTED")

                    val msgReply = Message()
                    msgReply.what = DISCONNECT
                    msgReply.obj = bluetoothDevice.name
                    mainThreadHandler!!.sendMessage(msgReply)

                    // Filters don't work
                    //var filter = ScanFilter.Builder().setDeviceName("Tiemerkkari").build()
                    //var filters: MutableList<ScanFilter> = mutableListOf(filter)
                    //var setting = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
                    //bluetoothAdapter?.bluetoothLeScanner?.startScan(filters, setting, bleScanCallback)

                    bluetoothAdapter.bluetoothLeScanner?.startScan( bleScanCallback)

                }

            }

            // When they are connected, sets itself
            // to listen for notifications of incoming messages
            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {

                super.onServicesDiscovered(gatt, status)

                Timber.tag("ThreadHandler").v("Notification listener set.")
                val service = gatt?.getService(SERVICE_UUID)
                val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID_RECEIVE)

                gatt?.setCharacteristicNotification(characteristic, true)
                characteristic?.value
            }

            // When messages are received,
            // the message will be send to MainThread to br used
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {

                super.onCharacteristicChanged(gatt, characteristic)

                val msgReply = Message()
                msgReply.what = RECEIVE

                if (characteristic != null) {

                    Timber.tag("ThreadHandler").v("Message received: ${characteristic.getStringValue(0)}")

                    msgReply.obj = characteristic.getStringValue(0)
                    mainThreadHandler!!.sendMessage(msgReply)


                }

            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)

                val msgReply = Message()
                msgReply.what = RECEIVE
                if (characteristic != null) {

                    Timber.tag("ThreadHandler").v("Message received: ${characteristic.getStringValue(0)}")

                    msgReply.obj = characteristic.getStringValue(0)
                    mainThreadHandler!!.sendMessage(msgReply)


                }
            }

        }

    }

}





