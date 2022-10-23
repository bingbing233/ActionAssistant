package com.example.actionassistant.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.actionassistant.base.BaseFragment
import com.example.actionassistant.databinding.FragmentMainBinding
import com.example.actionassistant.utils.checkAccessibilityServiceEnable
import com.example.actionassistant.utils.go2AccessibilitySettings

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCheckPermission.setOnClickListener {
            val enable = checkAccessibilityServiceEnable(requireContext())
            val msg = if(enable) "无障碍已授权" else "无障碍未授权"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
        binding.btnGoPermission.setOnClickListener {
            go2AccessibilitySettings(requireActivity())
        }
    }
}