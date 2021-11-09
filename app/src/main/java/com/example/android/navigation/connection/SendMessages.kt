package com.example.android.navigation.connection

import android.os.AsyncTask
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter

class SendMessages(msg: String) : AsyncTask<Void, String, Void>() {

    internal var msg = ""

    init {
        this.msg = msg
    }

    override fun doInBackground(vararg voids: Void): Void? {
        try {

            val out = PrintWriter(
                BufferedWriter(
                OutputStreamWriter(MyData.socket
                .getOutputStream())
            ), true)

            out.println(msg)
            println("message send")

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}