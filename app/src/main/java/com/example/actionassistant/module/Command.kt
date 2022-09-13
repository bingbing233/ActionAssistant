package com.example.actionassistant.module

import android.graphics.Point
import android.graphics.PointF
import java.io.Serializable

class Command :Serializable {
    var pkgName = ""
    var nodeName = ""
    var position = Point()
}