package com.example.actionassistant

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.actionassistant.databinding.ActivityMainBinding
import com.example.actionassistant.module.Command
import com.example.actionassistant.service.ActionService
import com.example.actionassistant.utils.checkAccessibilityServiceEnable
import com.example.actionassistant.utils.go2AccessibilitySettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val TAG = javaClass.simpleName
    private val serviceIntent by lazy {
        Intent(this, ActionService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoSetting.setOnClickListener {
            go2AccessibilitySettings(this)
        }

        binding.btnStartService.setOnClickListener {
            startService(serviceIntent)
        }

        binding.btnCheckPermission.setOnClickListener {
            val enable = checkAccessibilityServiceEnable(this)
            val text = if (enable) "无障碍权限已授予" else "无障碍权限未授予"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        binding.btnStopService.setOnClickListener {
            stopService(serviceIntent)
        }
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

