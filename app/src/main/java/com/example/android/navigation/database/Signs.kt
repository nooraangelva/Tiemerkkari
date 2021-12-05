package com.example.android.navigation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Sign data class

@Entity(tableName = "signs_info_table")
data class Signs  (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "sign_id")
        var signId: Long = 0L,

        @ColumnInfo(name = "sign_name")
        var signName: String =  "",

        @ColumnInfo(name = "source_picture")
        var sourcePicture: String = "",

        @ColumnInfo(name = "speed_area")
        var speedArea: Boolean = false,

        @ColumnInfo(name = "type")
        var type: Int = 0,

        @ColumnInfo(name = "info")
        var info: String =  "",

        //@ColumnInfo(name = "other_info")
        //val otherInfo: String =  "",
)