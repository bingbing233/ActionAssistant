package com.example.actionassistant.module

import android.graphics.Point
import android.graphics.PointF
import java.io.Serializable

class Command :Serializable {
    var pkgName = ""
    var nodeName = ""
    var position = Point()
    var type:Int = TYPE_CLICK

    companion object{
        const val TYPE_CLICK = 0
        const val TYPE_WAKE_UP_SCREEN = 1
        const val TYPE_OPEN_APP = 2

        fun getTypeContent(type:Int):String{
            return  when(type){
                Command.TYPE_CLICK -> "点击事件"
                Command.TYPE_OPEN_APP -> "打开App"
                else -> "无"
            }
        }
    }

    override fun toString(): String {
        return "Command(pkgName='$pkgName', nodeName='$nodeName', position=$position, type=$type)"
    }



}