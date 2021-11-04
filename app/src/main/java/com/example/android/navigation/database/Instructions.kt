package com.example.android.navigation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "printing_instructions_table")
data class Instructions  (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "step_id")
        var stepId: Long = 0L,

        @ColumnInfo(name = "sign_id")
        var signId: Long = 0L,

        @ColumnInfo(name = "Step")
        var step: Int = 0,

        @ColumnInfo(name = "directionX")
        var directionX: Boolean = false,

        @ColumnInfo(name = "directionY")
        var directionY: Boolean = false,

        @ColumnInfo(name = "order")
        var order: String = "",

        @ColumnInfo(name = "parameterX")
        var parX: Int = 0,

        @ColumnInfo(name = "parameterY")
        var parY: Int = 0,

        @ColumnInfo(name = "paint")
        var paint: Boolean = false,

)