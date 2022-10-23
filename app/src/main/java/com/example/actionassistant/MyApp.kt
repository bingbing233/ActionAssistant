package com.example.actionassistant

import android.app.Application
import android.content.Context

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
    }
}