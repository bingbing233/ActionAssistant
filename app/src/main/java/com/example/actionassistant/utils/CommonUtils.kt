package com.example.actionassistant.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import android.view.accessibility.AccessibilityManager


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

