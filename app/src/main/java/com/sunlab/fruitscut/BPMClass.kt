package com.sunlab.fruitscut

import android.os.SystemClock
import android.util.Log

class BPMClass:Thread() {

    var isRunning = false
    override fun run() {
        while(isRunning){
            SystemClock.sleep(1000)
            Log.d("쓰레드",System.currentTimeMillis().toString())
        }
    }

}