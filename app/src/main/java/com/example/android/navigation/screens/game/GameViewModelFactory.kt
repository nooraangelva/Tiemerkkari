package com.example.android.navigation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.navigation.screens.game.GameViewModel

class GameViewModelFactory (

        private val gameType: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(gameType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}