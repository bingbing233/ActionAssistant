package com.example.actionassistant

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val string = "type:faceswap|code:f6fc940f37db48c7|name:jacky"
        println( getValueFromOtherMsg("name",string))
    }

    fun getValueFromOtherMsg(key:String,otherMsg:String): String {
        val list = otherMsg.split('|')
        var value = ""
        var kv = ""
        list.forEach {
            if(it.contains(key)){
                kv = it
            }
        }

        if(kv != ""){
            value = kv.substringAfter(":")
        }
        return value
    }
}