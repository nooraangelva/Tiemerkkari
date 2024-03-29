package com.example.android.navigation.screens.sign_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.navigation.screens.imports.ImportViewModel

// Gets parameter to ViewModel
class SignTypeViewModelFactory(private val area: Boolean ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SignTypeViewModel::class.java)) {

            return SignTypeViewModel(area) as T

        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }

}