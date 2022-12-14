package com.example.actionassistant.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.actionassistant.databinding.FragmentMainBinding

abstract class BaseFragment <VB:ViewBinding>(private val block: (LayoutInflater)->VB): Fragment() {

    private lateinit var _binding:VB
    val binding get() = _binding
    val TAG = javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = block.invoke(inflater)
        return _binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ", )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ", )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated: ", )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView: ", )
    }
}