package com.example.android.navigation
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Receive(val a: Int, val b: String)

fun main() {
    val obj = Json.decodeFromString<Receive>("""{"a":42, "b": "str"}""")
}