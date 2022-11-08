package com.example.actionassistant.utils

import android.graphics.Point
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

object AdbUtils {
     val TAG = javaClass.simpleName.toString()
    fun execSuper(cmd: String) {
        kotlin.runCatching {
            val process = Runtime.getRuntime().exec("su")
            val os = process.outputStream
            os.write(cmd.toByteArray())
            os.flush()
            os.close()
        }.onFailure {
            Log.e(TAG, "exec super: failed",it )
        }
    }

    fun exec(cmd: String){
        kotlin.runCatching {
            val process = Runtime.getRuntime().exec(cmd)
            val stream = process.inputStream
            val buffer = BufferedReader(InputStreamReader(stream,"gbk"))
            Log.e(TAG, "exec: result = ${buffer.readText()}", )
            buffer.close()
            stream.close()
        }.onFailure {
            Log.e(TAG, "exec: failed", it )
        }
    }


    fun pressPowerKey(){
        execSuper("input keyevent 26")
    }

    fun openApp(pkgName:String){
        val cmd = "monkey -p $pkgName 1"
        execSuper(cmd)
    }

    fun click(point: Point){
        val cmd = "input tap ${point.x} ${point.y}"
    }
}