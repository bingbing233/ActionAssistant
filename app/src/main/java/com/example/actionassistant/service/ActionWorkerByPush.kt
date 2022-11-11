package com.example.actionassistant.service

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.actionassistant.MainViewModel
import com.example.actionassistant.module.Command
import com.example.actionassistant.utils.AdbUtils
import com.example.actionassistant.utils.TAG
import com.example.actionassistant.utils.isScreenOn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat

class ActionWorkerByPush(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val interval = 5000L //每五秒发送一次消息
    private val delay = 2000L //每个动作之间延迟两秒
    private val commands = arrayListOf<Command>()
    private var time = "00:00"
    private var running = true

    init {
        Log.e(TAG, "onInit: ")
        initData()
    }

    override fun doWork(): Result {
        runBlocking {
            flow {
                var curTime = ""
                while (true) {
                    if (commands.isEmpty() && running) {
                        break
                    }
                    delay(delay)
                    curTime = curTime()
                    emit(curTime)
                }
            }.collect {
                Log.e(TAG, "doWorkByPush: curTime = $it time = $time com = ${commands.size}")
                if (commands.isNotEmpty()) {
                    val command = commands.first()
                    when (command.type) {
                        Command.TYPE_CLICK -> {
                            AdbUtils.click(command.position)
                        }

                        Command.TYPE_WAKE_UP_SCREEN -> {
                            if (!isScreenOn(applicationContext)) {
                                AdbUtils.pressPowerKey()
                            }
                        }

                        Command.TYPE_OPEN_APP -> {
                            AdbUtils.openApp(pkgName = command.pkgName)
                        }
                    }
                    commands.removeFirst()
                }
            }
        }
        return Result.success()
    }

    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("HH:mm")
    private fun curTime(): String {
        return format.format(System.currentTimeMillis())
    }

    private fun initData() {
        commands.clear()
        commands.addAll(MainViewModel.getCommands())
        val wakeCommand = Command().apply { type = Command.TYPE_WAKE_UP_SCREEN }
        commands.add(0, wakeCommand)
        time = MainViewModel.getTime()
    }

    override fun onStopped() {
        super.onStopped()
        running = false
    }
}