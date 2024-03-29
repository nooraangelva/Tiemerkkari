package com.example.android.navigation.screens.printing

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.navigation.database.SignDatabaseDao

// Gets parameters to ViewModel
class PrintingViewModelFactory(private val signId: Long, private val dataSource: SignDatabaseDao, private val application: Application, private val bluetoothAdapter : BluetoothAdapter, private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrintingViewModel::class.java)) {
            return PrintingViewModel(signId, dataSource,application, bluetoothAdapter, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}