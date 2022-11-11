package com.example.actionassistant.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import org.greenrobot.eventbus.EventBus

const val TAG = "binghao"

fun go2AccessibilitySettings(activity: Activity) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    activity.startActivity(intent)
}

//点亮屏幕
fun wakeLock(context: Context) {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    powerManager.newWakeLock(
        PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.FULL_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
        context.javaClass.simpleName
    )
}



//是否点亮屏幕
fun isScreenOn(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isInteractive
}

fun checkAccessibilityServiceEnable(context: Context): Boolean {
    val accessibilityManager =
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    return accessibilityManager.isEnabled
}

/**
 * format hh:mm
 */
const val bigger = 0
const val smaller = 1
const val equal = 2
fun compareTime(time1:String,time2:String): Int {
    val t1 = time1.split(":")
    val t2 = time2.split(":")
    val h1 = t1[0].toInt()
    val m1 = t1[1].toInt()
    val h2 = t2[0].toInt()
    val m2 = t2[2].toInt()
    if(h1 > h2)
        return bigger
    if(h1 < h2)
        return smaller
    if(h1 == h2){
        return when{
            m1 > m2 -> bigger
            m1 < m2 -> smaller
            else -> equal
        }
    }
    return -1
}

fun registerEvent(obj:Any){
    EventBus.getDefault().register(obj)
}

fun unregisterEvent(obj: Any){
    EventBus.getDefault().unregister(obj)
}

fun postEvent(obj: Any){
    EventBus.getDefault().post(obj)
}