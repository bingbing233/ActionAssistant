package com.example.actionassistant.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.actionassistant.MainViewModel
import com.example.actionassistant.base.BaseFragment
import com.example.actionassistant.databinding.FragmentMainBinding
import com.example.actionassistant.service.ActionService
import com.example.actionassistant.utils.AdbUtils
import com.example.actionassistant.utils.checkAccessibilityServiceEnable
import com.example.actionassistant.utils.go2AccessibilitySettings
import kotlin.math.min

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel:MainViewModel by activityViewModels()
    private val serviceIntent by lazy {
        Intent(requireActivity(), ActionService::class.java)
    }

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

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val time = "$hourOfDay:$minute"
            viewModel.saveTime(time)
        }

        binding.btnStartService.setOnClickListener {
            val h = binding.timePicker.hour
            val m = binding.timePicker.minute
            val time = "$h:$m"
            Toast.makeText(requireContext(), "任务将在 $time 执行", Toast.LENGTH_SHORT).show()
            viewModel.saveTime(time)
            requireActivity().startService(serviceIntent)
        }

        binding.btnGetRoot.setOnClickListener {
            viewModel.accessRootPermission()
        }

    }
}