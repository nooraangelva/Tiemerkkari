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
        val sourcePicture: String =  "",

        @ColumnInfo(name = "speed_area")
        val speedArea: Boolean,

        @ColumnInfo(name = "type")
        val type: Int,

        @ColumnInfo(name = "measurements")
        val measurements: String =  "",

        @ColumnInfo(name = "other_info")
        val otherInfo: String =  "",
)