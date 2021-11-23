package com.example.android.navigation

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import java.lang.Math.sqrt

class ThreadHandler(val mainThreadHandler: Handler?) : Runnable{
    var workerThreadHandler : Handler? = null

    override fun run() {

        /*

        Looper.prepare()
        workerThreadHandler = object : Handler(Looper.myLooper()!!){
            override fun handleMessage(msg: Message) {
                Log.v("ThreadHandler", "painettu 2")

                if(IS_PRIME == msg.what){
                    Log.v("ThreadHandler", "ISPRIME")

                    val num : Int = msg.arg1

                    var i = 2
                    var flag = false
                    while (i <= num / 2) {
                        // condition for nonprime number
                        if (num % i == 0) {
                            flag = true
                            break
                        }
                        ++i
                    }

                    var msgReply = Message()
                    msgReply.what = IS_PRIME

                    if (!flag) {
                        Log.v("ThreadHandler", "$num is a prime number.")
                        msgReply.obj = "$num is a prime number."
                    }
                    else {
                        Log.v("ThreadHandler", "$num is not a prime number.")
                        msgReply.obj = "$num is not a prime number."
                    }

                    mainThreadHandler!!.sendMessage(msgReply)
                }
                else if(PRIME_NUMBERS == msg.what){
                    Log.v("ThreadHandler", "PRIME_NUMBERS")

                    val n : Int = msg.arg1
                    var status = 1
                    var num = 3

                    var numbersListing : String = "The first $n prime numbers are: "
                    if (n >= 1) {


                        //2 is a known prime number
                        numbersListing += "2 "
                    }


                    var i = 2
                    while (i <= n) {
                        var j = 2
                        while (j <= sqrt(num.toDouble())) {
                            if (num % j === 0) {
                                status = 0
                                break
                            }
                            j++
                        }
                        if (status !== 0) {
                            numbersListing += "$num "
                            i++
                        }
                        status = 1
                        num++
                    }


                    var msgReply = Message()
                    msgReply.what = PRIME_NUMBERS
                    msgReply.obj = numbersListing
                    mainThreadHandler!!.sendMessage(msgReply)
                }
                else if(PRIME == msg.what){
                    Log.v("ThreadHandler", "PRIME")


                    var n : Int = msg.arg1
                    // Array that contains all the prime factors of given number.
                    val arr: ArrayList<Int> = arrayListOf()

                    var numberText: String = "Factors for $n are: "

                    // At first check for divisibility by 2. add it in arr till it is divisible
                    while (n % 2 == 0) {
                        arr.add(2)
                        n /= 2
                    }
                    val squareRoot = sqrt(n.toDouble()).toInt()

                    // Run loop from 3 to square root of n. Check for divisibility by i. Add i in arr till it is divisible by i.
                    for (i in 3..squareRoot step 2) {
                        while (n % i == 0) {
                            arr.add(i)
                            n /= i
                        }
                    }

                    // If n is a prime number greater than 2.
                    if (n > 2) {
                        arr.add(n)
                    }


                    for (number in arr) {
                        numberText += "$number "
                    }


                    var msgReply = Message()
                    msgReply.what = PRIME
                    msgReply.obj = numberText
                    mainThreadHandler!!.sendMessage(msgReply)
                }

            }
        }
        Looper.loop()

        Log.v("ThreadHandler", "class SimpleRunnable current ${Thread.currentThread()}")
        var msg = Message()
        mainThreadHandler!!.sendMessage(msg)*/
    }
}





