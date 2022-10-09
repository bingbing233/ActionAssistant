package com.example.actionassistant

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.actionassistant.base.BaseActivity
import com.example.actionassistant.databinding.ActivityMainBinding
import com.example.actionassistant.module.Command
import com.example.actionassistant.service.ActionService
import com.example.actionassistant.ui.add.AddActivity
import com.example.actionassistant.ui.add.SP_COMMAND
import com.example.actionassistant.ui.add.commands
import com.example.actionassistant.utils.AdbUtils
import com.example.actionassistant.utils.checkAccessibilityServiceEnable
import com.example.actionassistant.utils.go2AccessibilitySettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val TAG = javaClass.simpleName

    private val serviceIntent by lazy {
        Intent(this, ActionService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initData()
    }

    private fun initData() {
        val sp = getSharedPreferences(SP_COMMAND, Context.MODE_PRIVATE)
        val string = sp.getString(SP_COMMAND, "")!!
        if (string.isNotBlank()) {
            val gson = Gson()
            val type = object : TypeToken<List<Command>>() {}.type
            val list: List<Command> = gson.fromJson(string, type)
            commands.addAll(list)
            Log.e("TAG", "initData: $list")
        }
    }
}

