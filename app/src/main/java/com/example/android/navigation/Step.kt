package com.example.android.navigation

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Step(

                var step: String = "",

                var order: Int = -1,

                var parX: String = "",

                var parY: String = "",

                var paint: Int = -1,)
