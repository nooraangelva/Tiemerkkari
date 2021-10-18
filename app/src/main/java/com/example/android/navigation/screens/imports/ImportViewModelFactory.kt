package com.example.android.navigation.screens.imports


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImportViewModelFactory(private val finalScore: Int, private val finalTime: Int,
                             private val finalQuestions: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImportViewModel::class.java)) {
            return ImportViewModel(finalScore, finalTime, finalQuestions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}