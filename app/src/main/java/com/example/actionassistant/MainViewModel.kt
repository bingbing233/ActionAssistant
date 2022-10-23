package com.example.actionassistant

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actionassistant.module.Command
import com.example.actionassistant.ui.add.SP_COMMAND
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    companion object {
        const val SP_COMMAND = "command"
        fun initAllData(): List<Command> {
            val sp = MyApp.getAppContext().getSharedPreferences(SP_COMMAND, Context.MODE_PRIVATE)
            val string = sp.getString(SP_COMMAND, "")!!
            return if (string.isNotBlank()) {
                val gson = Gson()
                val type = object : TypeToken<List<Command>>() {}.type
                val list: List<Command> = gson.fromJson(string, type)
                list
            }else{
                emptyList()
            }
        }
    }

    private val TAG = javaClass.simpleName

    private val _commands = MutableStateFlow<MutableList<Command>>(arrayListOf())
    val commands = _commands.asStateFlow()

    fun updateCommands(list: List<Command>) {
        viewModelScope.launch {
            _commands.emit(list as MutableList<Command>)
        }
    }


    fun updateCommandAt(index: Int, command: Command) {
        viewModelScope.launch {
            val list = _commands.value
            list[index] = command
            _commands.emit(list)
        }
    }

    fun addCommand(command: Command) {
        viewModelScope.launch {
            val list = ArrayList(_commands.value)
            list.add(command)
            _commands.emit(list)
        }
    }

    fun removeCommand(index: Int) {
        viewModelScope.launch {
            val list = ArrayList(_commands.value)
            list.removeAt(index)
            _commands.emit(list)
        }
    }

    fun initData() {
        viewModelScope.launch {
            val sp = MyApp.getAppContext().getSharedPreferences(SP_COMMAND, Context.MODE_PRIVATE)
            val string = sp.getString(SP_COMMAND, "")!!
            if (string.isNotBlank()) {
                val gson = Gson()
                val type = object : TypeToken<List<Command>>() {}.type
                val list: List<Command> = gson.fromJson(string, type)
                Log.e(TAG, "initData: $list")
                _commands.emit(list as MutableList<Command>)
            }
        }
    }

    fun saveData() {
        val sp = MyApp.getAppContext().getSharedPreferences(SP_COMMAND, Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val string = gson.toJson(_commands.value)
        Log.e("TAG", "save: $string")
        sp.putString(SP_COMMAND, string)
        sp.apply()
    }

}