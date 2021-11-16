package com.example.android.navigation.screens.printing

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.navigation.database.SignDatabaseDao


class PrintingViewModelFactory(private val signId: Long, private val dataSource: SignDatabaseDao, private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrintingViewModel::class.java)) {
            return PrintingViewModel(signId, dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}