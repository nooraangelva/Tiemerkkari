package com.example.android.navigation

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.lang.Math.sqrt

class ThreadHandler(val mainThreadHandler: Handler?) : Runnable{


    private var bluetoothAdapter: BluetoothAdapter? = null

    @Synchronized
    fun setBluetoothAdapter(value: BluetoothAdapter?){
        bluetoothAdapter = value
    }

    var workerThreadHandler : Handler? = null


    override fun run() {


        Looper.prepare()
        workerThreadHandler = object : Handler(Looper.myLooper()!!) {

            override fun handleMessage(msg: Message) {



                    if (SEND == msg.what) {
                        Timber.tag("ThreadHandler").v("ISPRIME")

                        val data = msg.obj

                        val service : BluetoothGattService? = gatt?.getService(SERVICE_UUID_SEND)
                        val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID_SEND)
                        characteristic!!.setValue(data.toString())

                        gatt?.writeCharacteristic(characteristic)


                        var msgReply = Message()
                        msgReply.what = SEND


                        msgReply.obj = "$num is not a prime number."
                        mainThreadHandler!!.sendMessage(msgReply)
                    }
                    else if (RECEIVE == msg.what) {
                        Timber.tag("ThreadHandler").v("ISPRIME")

                        val num: Int = msg.arg1
                        var msgReply = Message()
                        msgReply.what = RECEIVE
                    }
                    else if (CONNECT == msg.what) {
                        Timber.tag("ThreadHandler").v("ISPRIME")

                        var filter = ScanFilter.Builder().setDeviceName("Tiemerkkari").build()
                        var filters: MutableList<ScanFilter> = mutableListOf(filter)

                        var setting = ScanSettings.Builder().build()
                        bluetoothAdapter?.bluetoothLeScanner?.startScan(filters, setting, bleScanCallback)


                        var msgReply = Message()
                        msgReply.what = CONNECT
                    }



            }
            }
            Looper.loop()

            Timber.v("class SimpleRunnable current " + Thread.currentThread())
            var msg = Message()
            mainThreadHandler!!.sendMessage(msg)

    }

    fun connectToDevice(bluetoothDevice: BluetoothDevice) {

        bluetoothDevice.connectGatt( , false, bleGattCallback)

    }

    private val bleScanCallback: ScanCallback by lazy {
        object : ScanCallback() {

            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                val bluetoothDevice = result.device
                connectToDevice(bluetoothDevice)

                Timber.tag("Buttons").v("laite: %s", bluetoothDevice.name)

                if (bluetoothDevice != null) {
                    connectToDevice(bluetoothDevice)
                }
            }
        }
    }
    private val bleGattCallback: BluetoothGattCallback by lazy {
        object : BluetoothGattCallback() {
            override fun onConnectionStateChange(
                gatt: BluetoothGatt?,
                status: Int,
                newState: Int
            ) {
                super.onConnectionStateChange(gatt, status, newState)

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt?.discoverServices()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)


                val service = gatt?.getService(SERVICE_UUID_RECEIVE)
                val characteristic =
                    service?.getCharacteristic(CHARACTERISTIC_UUID_RECEIVE)

                gatt?.setCharacteristicNotification(characteristic, true)
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                super.onCharacteristicChanged(gatt, characteristic)

                var msgReply = Message()
                msgReply.what = RECEIVE

                Timber.v("laite: " + characteristic?.getStringValue(0))
                if (characteristic != null) {

                    msgReply.obj =
                        Json.decodeFromString<Receive>(characteristic.getStringValue(0))
                    mainThreadHandler!!.sendMessage(msgReply)
                }
            }

        }
    }
}





