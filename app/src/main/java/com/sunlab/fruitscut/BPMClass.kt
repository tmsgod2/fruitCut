package com.sunlab.fruitscut

import android.os.SystemClock
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.util.*
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

object BPMObj {


    var time = 1000
    var isRunning = false
    var timerTask: Timer?=null

    fun start(times:Int=1000){
        isRunning = true
        time = times
        timerTask = timer(period = 20){
            time++
            val sec = time/100
            val milli = time%100

            Log.d("쓰레드", sec.toString())

        }
    }
    fun pause(){
        isRunning = false
        timerTask?.cancel()
    }



}