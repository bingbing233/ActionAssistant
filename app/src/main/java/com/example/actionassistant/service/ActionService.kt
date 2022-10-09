package com.example.actionassistant.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.graphics.Point
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.example.actionassistant.module.Command
import com.example.actionassistant.ui.add.commands
import com.example.actionassistant.utils.AdbUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class ActionService : AccessibilityService() {

    val TAG = javaClass.simpleName
    var nodeInfo: AccessibilityNodeInfo? = null
    var clickFlag = true
    lateinit var list: ArrayList<Command>
    val delay = 1000L

    //每次进行界面切换会触发
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        nodeInfo = event?.source
        nodeInfo?.let {
            if (list.isNotEmpty()) {
                clickFlag = true
                val command = list.first()
                if (it.packageName == command.pkgName) {
                    runBlocking {
                        delay(delay)
                        click(command.position)
                        list.removeFirst()
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
        list = ArrayList(commands)
        Log.e(TAG, "onServiceConnected: command size = ${list.size}")
        Toast.makeText(applicationContext, "无障碍服务已连接", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
        Toast.makeText(applicationContext, "无障碍服务已关闭", Toast.LENGTH_SHORT).show()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }


    private fun clickHome() {
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    private fun click(text: String) {
        val node = nodeInfo?.findAccessibilityNodeInfosByText(text)
        node?.let {
            if (node.size > 0) {
                kotlin.runCatching {
                    val result = node[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Log.e(TAG, "click $text: suc,result = ${node.get(0)}")
                }.onFailure {
                    Log.e(TAG, "click: fail",it )
                }
            }
        }
    }

    private fun click(point: Point){
        val cmd = "input tap ${point.x} ${point.y} \n"
        AdbUtils.exec(cmd)
    }
}