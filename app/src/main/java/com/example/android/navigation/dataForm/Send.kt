package com.example.android.navigation.dataForm

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class Send(val a: Int, val b: String)


fun main() {
    val dataList = listOf(Send(42, "str"), Send(12, "test"))
    val json = Json.encodeToString(Send(42, "str"))
}
