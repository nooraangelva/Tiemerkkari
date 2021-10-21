package com.example.android.navigation.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SignDatabaseDao {

    // TABLE SIGNS QUERYES

    @Insert
    suspend fun insertSign(sign: Signs)

    @Update
    suspend fun updateSigns(sign: Signs)

    @Query("SELECT * from signs_info_table")
    suspend fun getSign(): LiveData<List<Signs>>

    @Query("SELECT * from signs_info_table WHERE type = :key AND speed_area = :key2")
    suspend fun filterGetSigns(key: Boolean, key2: Boolean): LiveData<List<Signs>>

    @Query("DELETE FROM signs_info_table")
    suspend fun clearAllSign()

    @Query("DELETE FROM signs_info_table WHERE sign_id = :key")
    suspend fun clearSign(key: Long): Signs?

    //TABLE INSTRUCTIONS QUERYES

    @Insert
    suspend fun insertIns(step: Instructions)

    @Update
    suspend fun updateIns(step:Instructions)

    @Query("SELECT * from printing_instructions_table WHERE step_id = :key")
    suspend fun getIns(key: Long): LiveData<List<Instructions>>

    @Query("SELECT * from printing_instructions_table WHERE sign_id = :key ORDER BY step")
    suspend fun filterGetIns(key: Long): LiveData<List<Instructions>>

    @Query("DELETE FROM printing_instructions_table")
    suspend fun clearAllIns()

    @Query("DELETE FROM printing_instructions_table WHERE sign_id = :key")
    suspend fun clearIns(key: Long): Instructions?

}