package com.example.android.navigation.dataForm

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Step(

                var step: Int = 0,

                var order: Int = -1,

                var parX: String = "",

                var parY: String = "",

                var paint: Int = -1,)
