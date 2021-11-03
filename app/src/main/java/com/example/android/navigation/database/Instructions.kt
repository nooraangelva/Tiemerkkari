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
        val signId: Long = 0L,

        @ColumnInfo(name = "Step")
        val step: Int = 0,

        @ColumnInfo(name = "directionX")
        val directionX: Boolean = false,

        @ColumnInfo(name = "directionY")
        val directionY: Boolean = false,

        @ColumnInfo(name = "order")
        val order: String = "",

        @ColumnInfo(name = "parameterX")
        val parX: Int = 0,

        @ColumnInfo(name = "parameterY")
        val parY: Int = 0,

        @ColumnInfo(name = "paint")
        val paint: Boolean = false,

)