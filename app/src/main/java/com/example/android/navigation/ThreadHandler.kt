package com.example.android.navigation

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import timber.log.Timber
import java.lang.Math.sqrt

class ThreadHandler(val mainThreadHandler: Handler?) : Runnable{
    var workerThreadHandler : Handler? = null

    override fun run() {


        Looper.prepare()
        workerThreadHandler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                Timber.v("painettu 2")

                if (SEND == msg.what) {
                    Timber.tag("ThreadHandler").v("ISPRIME")

                    val num: Int = msg.arg1
                    var msgReply = Message()
                    msgReply.what = SEND


                    msgReply.obj = "$num is not a prime number."
                    mainThreadHandler!!.sendMessage(msgReply)
                } else if (RECEIVE == msg.what) {
                    Timber.tag("ThreadHandler").v("ISPRIME")

                    val num: Int = msg.arg1
                    var msgReply = Message()
                    msgReply.what = RECEIVE
                }


            }
        }
        Looper.loop()

        Timber.v("class SimpleRunnable current " + Thread.currentThread())
        var msg = Message()
        mainThreadHandler!!.sendMessage(msg)
    }
}





