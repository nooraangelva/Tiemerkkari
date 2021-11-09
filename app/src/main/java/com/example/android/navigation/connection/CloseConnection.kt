package com.example.android.navigation.connection

import android.os.AsyncTask
import java.io.IOException

class CLoseConnection : AsyncTask<Void, String, Void>(){

    override fun doInBackground(vararg params: Void?): Void? {
        try {

            MyData.socket?.close()
            System.out.println("connection closed")

        }catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

}