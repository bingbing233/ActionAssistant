package com.example.actionassistant.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.example.actionassistant.commands
import com.example.actionassistant.module.Command

class ActionService : AccessibilityService() {

    val TAG = javaClass.simpleName
    var nodeInfo: AccessibilityNodeInfo?=null
    var clickFlag = true
    lateinit var list: ArrayList<Command>
    val delay = 1000

    //每次进行界面切换会触发
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        nodeInfo = event?.source
        nodeInfo?.let {
            if(list.isNotEmpty()){
                clickFlag = true
                val command = list.first()
                if (it.packageName == command.pkgName) {
                    click(command.nodeName)
                    list.removeFirst()
                    Log.e(TAG, " click pkg = ${it.packageName} , ${command.nodeName}", )
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.e(TAG, "onInterrupt: ", )
    }


    //服务链接成功时触发
    override fun onServiceConnected() {
        super.onServiceConnected()
        list = ArrayList(commands)
        Log.e(TAG, "onServiceConnected: ${list.size}", )
        Toast.makeText(applicationContext, "无障碍服务已连接", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: ", )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ", )
        Toast.makeText(applicationContext, "无障碍服务已关闭", Toast.LENGTH_SHORT).show()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: ", )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(TAG, "onUnbind: ", )
        return super.onUnbind(intent)
    }


    private fun clickHome() {
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    private fun click(text:String){
        val node = nodeInfo?.findAccessibilityNodeInfosByText(text)
        node?.let {
            if(node.size>0 && clickFlag){
                    clickFlag = false
                node[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }
}