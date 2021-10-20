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
        val signId: Long,

        @ColumnInfo(name = "Step")
        val step: Int,

        @ColumnInfo(name = "order")
        val order: String,

)