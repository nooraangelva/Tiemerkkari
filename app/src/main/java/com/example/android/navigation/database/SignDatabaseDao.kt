package com.example.android.navigation.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SignDatabaseDao {

    // TABLE SIGNS QUERYES

    @Insert
    fun insertSign(sign: Signs)

    @Update
    fun updateSigns(sign: Signs)

    @Query("SELECT * from signs_info_table WHERE sign_id = :key")
    fun getSign(key: Long): Signs?

    @Query("SELECT * from signs_info_table WHERE type = :key AND speed_area = :key2")
    fun filterGetSigns(key: Boolean, key2: Boolean): Signs?

    @Query("DELETE FROM signs_info_table")
    fun clearAllSign()

    @Query("DELETE FROM signs_info_table WHERE sign_id = :key")
    fun clearSign(key: Long): Signs?

    //TABLE INSTRUCTIONS QUERYES

    @Insert
    fun insertIns(step: Instructions)

    @Update
    fun updateIns(step:Instructions)

    @Query("SELECT * from printing_instructions_table WHERE step_id = :key")
    fun getIns(key: Long): Instructions?

    @Query("SELECT * from printing_instructions_table WHERE sign_id = :key ORDER BY step")
    fun filterGetIns(key: Long): Instructions?

    @Query("DELETE FROM printing_instructions_table")
    fun clearAllIns()

    @Query("DELETE FROM printing_instructions_table WHERE sign_id = :key")
    fun clearIns(key: Long): Instructions?

}