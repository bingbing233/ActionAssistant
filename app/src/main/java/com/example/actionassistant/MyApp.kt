package com.example.actionassistant

import android.app.Application
import android.content.Context
import android.util.Log
import cn.jpush.android.api.JPushInterface

class MyApp : Application() {

    companion object{
        private lateinit var application: Application
        fun getAppContext(): Context {
            return application
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initJPush()
    }


    private fun initJPush(){
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        val id = JPushInterface.getRegistrationID(this)
        Log.e("binghao", "initJPush: $id", )
    }
}