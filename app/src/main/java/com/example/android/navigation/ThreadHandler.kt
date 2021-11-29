package com.example.android.navigation

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import timber.log.Timber
import java.lang.Math.sqrt

class ThreadHandler(val mainThreadHandler: Handler?, val thisContext : Context, val bluetoothAdapter: BluetoothAdapter) : Runnable{


    var workerThreadHandler : Handler? = null
    var bluetoothGatt : BluetoothGatt? = null
    lateinit var bluetoothDevice : BluetoothDevice


    override fun run() {


        Looper.prepare()
        workerThreadHandler = object : Handler(Looper.myLooper()!!) {

            override fun handleMessage(msg: Message) {



                    if (SEND == msg.what) {
                        Timber.tag("ThreadHandler").v("ISPRIME")

                        val data = msg.obj

                        val service : BluetoothGattService? = bluetoothGatt?.getService(SERVICE_UUID)
                        val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID_SEND)
                        characteristic!!.setValue(data.toString())

                        bluetoothGatt?.writeCharacteristic(characteristic)
                        bluetoothGatt?.setCharacteristicNotification(characteristic,true)


                        val msgReply = Message()
                        msgReply.what = SEND
                        if(data.toString().contains("STOP",true)){
                            msgReply.obj = "Printing stopped."
                        }
                        else {
                            msgReply.obj = "Printing started."
                        }
                        mainThreadHandler!!.sendMessage(msgReply)

                    }
                    else if (CONNECT == msg.what) {

                        Timber.tag("ThreadHandler").v("ISPRIME")

                        //var filter = ScanFilter.Builder().setDeviceName("TONIBLE").build()
                        //var filters: MutableList<ScanFilter> = mutableListOf(filter)
                        //var setting = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
                        //bluetoothAdapter?.bluetoothLeScanner?.startScan(filters, setting, bleScanCallback)
                        bluetoothAdapter.bluetoothLeScanner?.startScan( bleScanCallback)

                    }



            }
            }
            Looper.loop()

    }

    fun connectToDevice(bluetoothDevice: BluetoothDevice) {

        bluetoothGatt = bluetoothDevice.connectGatt( thisContext, false, bleGattCallback)

        val msgReply = Message()
        msgReply.what = CONNECT
        msgReply.obj = bluetoothDevice.name
        mainThreadHandler!!.sendMessage(msgReply)

        bluetoothAdapter.bluetoothLeScanner?.stopScan(bleScanCallback)

    }

    private val bleScanCallback: ScanCallback by lazy {
        object : ScanCallback() {

            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                bluetoothDevice = result.device

                //Timber.tag("Buttons").v("laite: %s", bluetoothDevice.name)

                if(!bluetoothDevice.name.isNullOrEmpty()) {
                    if (bluetoothDevice.name.contains("Tiemerkkari", true)) {
                        connectToDevice(bluetoothDevice)
                    }
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
                    bluetoothGatt?.discoverServices()
                }
                else{
                    //var filter = ScanFilter.Builder().setDeviceName("TONIBLE").build()
                    //var filters: MutableList<ScanFilter> = mutableListOf(filter)
                    //var setting = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
                    //bluetoothAdapter?.bluetoothLeScanner?.startScan(filters, setting, bleScanCallback)
                    bluetoothAdapter.bluetoothLeScanner?.startScan( bleScanCallback)
                }

            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)


                val service = gatt?.getService(SERVICE_UUID)
                val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID_RECEIVE)

                gatt?.setCharacteristicNotification(characteristic, true)
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                super.onCharacteristicChanged(gatt, characteristic)

                val msgReply = Message()
                msgReply.what = RECEIVE

                //Timber.v("laite: " + characteristic?.getStringValue(0))

                if (characteristic != null) {


                    msgReply.obj = characteristic.getStringValue(0)
                    mainThreadHandler!!.sendMessage(msgReply)


                }
            }

        }
    }
}





