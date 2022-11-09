package com.example.actionassistant.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.actionassistant.MainViewModel
import com.example.actionassistant.base.BaseFragment
import com.example.actionassistant.databinding.FragmentMainBinding
import com.example.actionassistant.service.ActionWorker

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel:MainViewModel by activityViewModels()
    private val serviceIntent by lazy {
        Intent(requireActivity(), ActionService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val h = binding.timePicker.hour
            val m = binding.timePicker.minute
            var hText = h.toString()
            var mText = m.toString()
            if(m < 10){
                mText = "0$m"
            }
            if(h < 10){
                hText = "0$h"
            }
            val time = "$hText:$mText"
            viewModel.saveTime(time)
        }

        binding.btnStartService.setOnClickListener {
            val workRequest = OneTimeWorkRequestBuilder<ActionWorker>().build()
            WorkManager.getInstance(requireContext()).enqueue(workRequest)
        }

        binding.btnStopService.setOnClickListener {
            WorkManager.getInstance(requireContext()).cancelAllWork()
        }
        binding.btnGetRoot.setOnClickListener {
            viewModel.accessRootPermission()
        }
    }
}