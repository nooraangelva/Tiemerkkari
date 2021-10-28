package com.example.android.navigation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signs_info_table")
data class Signs  (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "sign_id")
        var signId: Long = 0L,

        @ColumnInfo(name = "sign_name")
        val signName: String =  "",

        @ColumnInfo(name = "source_picture")
        val sourcePicture: Int = 0,

        @ColumnInfo(name = "speed_area")
        val speedArea: Boolean = false,

        @ColumnInfo(name = "type")
        val type: Int = 0,

        @ColumnInfo(name = "info")
        val info: String =  "",

        //@ColumnInfo(name = "other_info")
        //val otherInfo: String =  "",
)