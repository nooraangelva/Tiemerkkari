package com.example.android.navigation.screens.speed_area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.navigation.screens.speed_area.SpeedAreaViewModel

class SpeedAreaViewModelFactory( ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpeedAreaViewModel::class.java)) {
            return SpeedAreaViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}