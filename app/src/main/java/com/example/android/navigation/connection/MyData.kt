package com.example.android.navigation.connection

import com.example.android.navigation.MainActivity
import java.net.Socket

object MyData {
    lateinit var socket: Socket
    lateinit var mainActivity : MainActivity
    var THREAD_RUNNING = false
}