package com.example.android.navigation.connection

import android.os.AsyncTask
import java.io.IOException
import java.net.Socket

    class OpenConnection(private val ipAddress: String, private val portNumber: Int) : AsyncTask<Void, String, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            try {
                MyData.socket = Socket(ipAddress, portNumber)
                System.out.println("connection opened")

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }
    }
