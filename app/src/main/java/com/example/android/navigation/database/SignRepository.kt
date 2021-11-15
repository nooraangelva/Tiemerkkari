package com.example.android.navigation.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class SignRepository(private val signDao: SignDatabaseDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<Signs>> = signDao.getSign()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(sign: Signs) {
        signDao.insertSign(sign)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getInsFromDatabase(signId: Long): Flow<List<Instructions>> {

        return signDao.getIns(signId)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getSignsFromDatabase(type: Int, area : Boolean): Flow<List<Signs>> {

        return signDao.filterGetSigns(type,area)

    }

}