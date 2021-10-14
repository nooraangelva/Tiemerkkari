package com.example.android.navigation.screens.score

import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.navigation.R

class ScoreViewModel (finalScore: Int, finalTime: Int, finalQuestions: Int, gameTypeChoise: Int) : ViewModel(){

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    private val _eventPlayNewGame = MutableLiveData<Boolean>()
    val eventPlayNewGame: LiveData<Boolean>
        get() = _eventPlayNewGame

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _gameTime = MutableLiveData<Int>()
    val gameTime: LiveData<Int>
        get() = _gameTime

    private val _numQuestions = MutableLiveData<Int>()
    val numQuestions: LiveData<Int>
        get() = _numQuestions

    private val _gameType = MutableLiveData<Int>()
    val gameType: LiveData<Int>
        get() = _gameType

    val tripleMediatorLiveData = TripleMediatorLiveData(_score, _gameTime, _numQuestions)

    init {
        _score.value = finalScore
        _gameTime.value = finalTime
        _numQuestions.value = finalQuestions
        _gameTime.value = gameTypeChoise

    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }

    fun onPlayNewGame() {
        _eventPlayNewGame.value = true
    }

    fun onPlayNewGameComplete() {
        _eventPlayNewGame.value = false
    }
}