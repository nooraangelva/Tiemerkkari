package com.example.android.navigation.screens.score


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScoreViewModelFactory(private val finalScore: Int, private val finalTime: Int,
                            private val finalQuestions: Int, private val gameType: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(finalScore, finalTime, finalQuestions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}