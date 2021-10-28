package com.example.android.navigation.screens.imports

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.navigation.database.Instructions
import com.example.android.navigation.database.SignDatabaseDao
import com.example.android.navigation.database.Signs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ImportViewModel (val dataSource: SignDatabaseDao, application: Application) : ViewModel(){

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private val _sign = MutableLiveData<Signs?>()
    val sign: LiveData<Signs?>
        get() = _sign

    private val _step = MutableLiveData<Instructions?>()
    val step: LiveData<Instructions?>
        get() = _step



    init {


    }

    //FUNCTIONS

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}