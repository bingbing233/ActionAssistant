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
        Log.e(TAG, "onAccessibilityEvent: time = $time  curTime = ${curTime()} commands = ${commands.size}", )
        nodeInfo = event?.source
        nodeInfo?.let {
            if (commands.isNotEmpty()) {
                val command = commands.first()
                Log.e(TAG, "时间到，执行任务 ${command.pkgName} ${it.packageName}", )
                if (it.packageName == command.pkgName) {
                    runBlocking {
                        delay(delay)
                        click(command.position)
                        commands.removeFirst()
                        Log.e(TAG, " click pkg = ${it.packageName} ,position = ${command.position}")
                    }
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
        time = MainViewModel.getTime()
        Log.e(TAG, "onServiceConnected: command size = ${commands.size} time = $time")
        Toast.makeText(applicationContext, "无障碍服务已连接,command step = ${commands.size} time = $time", Toast.LENGTH_SHORT).show()
//        openRimet()
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

    private fun clickHome() {
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    private fun click(point: Point){
        val cmd = "input tap ${point.x} ${point.y} \n"
        AdbUtils.exec(cmd)
    }

    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("HH:mm")
    private fun curTime(): String? {
        return format.format(System.currentTimeMillis())
    }

    private fun openRimet(){
        val pkgName = "com.alibaba.android.rimet"
        val resolveIntent = Intent(Intent.ACTION_MAIN,null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(pkgName)
        }
        val apps = packageManager.queryIntentActivities(resolveIntent,0)
        val resolve = apps.iterator().next()
        if(resolve!=null){
            val className = resolve.activityInfo.name
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val cn = ComponentName(pkgName,className)
            intent.component = cn
            startActivity(intent)
        }
    }
}