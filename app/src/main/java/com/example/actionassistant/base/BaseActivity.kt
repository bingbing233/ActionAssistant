package com.example.actionassistant.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB:ViewBinding>(private val block:(LayoutInflater)->VB):AppCompatActivity() {

    private lateinit var _binding:VB
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = block.invoke(layoutInflater)
        setContentView(_binding.root)
    }
}