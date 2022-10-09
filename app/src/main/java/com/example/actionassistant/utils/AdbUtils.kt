package com.example.actionassistant.utils

import android.util.Log

object AdbUtils {
     val TAG = javaClass.simpleName.toString()
    fun exec(cmd: String) {
        kotlin.runCatching {
            val os = Runtime.getRuntime().exec("su").outputStream
            os.write(cmd.toByteArray())
            os.flush()
        }.onFailure {
            Log.e(TAG, "exec: failed",it )
        }
    }
}