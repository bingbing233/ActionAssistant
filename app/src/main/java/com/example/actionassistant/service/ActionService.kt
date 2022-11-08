package com.example.actionassistant.service

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.graphics.Point
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.example.actionassistant.MainViewModel
import com.example.actionassistant.module.Command
import com.example.actionassistant.utils.AdbUtils
import com.example.actionassistant.utils.Constant
import com.example.actionassistant.utils.isScreenOn
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat

class ActionService : AccessibilityService() {
    private val commands = arrayListOf<Command>()
    private val TAG = javaClass.simpleName
    private var nodeInfo: AccessibilityNodeInfo? = null
    private val delay = 2000L
    private var time = "00:00"

    //每次进行界面切换会触发
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        nodeInfo = event?.source
        nodeInfo?.let {
            Log.e(TAG, "event:time = $time curTime = ${curTime()} com = ${commands.size}", )
            if (commands.isNotEmpty() && time == curTime()) {
                val command = commands.first()
                Log.e(TAG, "time up: $command", )
                runBlocking {
                    delay(delay)
                    when(command.type){
                        Command.TYPE_CLICK -> {
                            AdbUtils.click(command.position)
                        }
                        Command.TYPE_WAKE_UP_SCREEN -> {
                            Log.e(TAG, "screenOn:${isScreenOn(applicationContext)} ", )
                            if (!isScreenOn(applicationContext)) {
                                AdbUtils.pressPowerKey()
                            }
                        }
                        Command.TYPE_OPEN_APP -> {
                            AdbUtils.openApp(pkgName = command.pkgName)
                        }
                    }
                    commands.removeFirst()
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.e(TAG, "onInterrupt: ")
    }

    //服务链接成功时触发
    override fun onServiceConnected() {
        super.onServiceConnected()
        commands.clear()
        commands.addAll(MainViewModel.getCommands())
        val wakeCommand = Command().apply { type = Command.TYPE_WAKE_UP_SCREEN }
        commands.add(0,wakeCommand)
        time = MainViewModel.getTime()
        Log.e(TAG, "onServiceConnected: command size = ${commands.size} time = $time")
        Toast.makeText(
            applicationContext,
            "无障碍服务已连接,command step = ${commands.size} time = $time",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: ")
    }

    /**
     * 服务关闭时会走onDestroy 和 onUnbind
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
        Toast.makeText(applicationContext, "无障碍服务已关闭", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("HH:mm")
    private fun curTime(): String? {
        return format.format(System.currentTimeMillis())
    }


}