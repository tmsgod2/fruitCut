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

object timeObj {

    var time = 0
    var isRunning = false
    var timerTask: Timer?=null
    var sec = 0
    var min = 0
    var hour = 0

     fun start(){
         timerTask = null
         isRunning = true
         allZero()
         timerTask = timer(period = 1000){
            sec++
            if(sec==60){
                min++
                sec = 0
            }
            if(min == 60){
                hour++
                min = 0
            }
        }
    }
    fun pause(){
        isRunning = false
        timerTask?.cancel()
        timerTask = null
    }

    fun getTime():String{
        return "$hour : $min : $sec"
        allZero()
    }

    fun allZero(){
        sec = 0
        min = 0
        hour = 0
    }





}
class BPMClass{

}