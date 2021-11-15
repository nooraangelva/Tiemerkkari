package com.example.android.navigation.screens.sign_options

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.navigation.database.SignApplication
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.SignRepository

class SignOptionsViewModelFactory(

    private val area: Boolean, private val type: Int, private val dataSource: SignDatabaseDao, private val application: SignApplication) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignOptionsViewModel::class.java)) {
            return SignOptionsViewModel(area, type, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}