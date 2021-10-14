package com.example.android.navigation.screens.sign_options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignOptionsViewModelFactory (

        private val gameType: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignOptionsViewModel::class.java)) {
            return SignOptionsViewModel(gameType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}