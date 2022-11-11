package com.example.actionassistant.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.actionassistant.MainViewModel
import com.example.actionassistant.base.BaseFragment
import com.example.actionassistant.databinding.FragmentMainBinding
import com.example.actionassistant.service.ActionWorker
import com.example.actionassistant.service.ActionWorkerByPush
import com.example.actionassistant.utils.ActionEvent
import com.example.actionassistant.utils.registerEvent
import com.example.actionassistant.utils.unregisterEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel:MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerEvent(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterEvent(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timePicker.setOnTimeChangedListener { _, _, _ ->
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
            startWork()
        }

        binding.btnStopService.setOnClickListener {_->
           WorkManager.getInstance(requireContext()).cancelAllWork()
        }
        binding.btnGetRoot.setOnClickListener {
            viewModel.accessRootPermission()
        }
    }

    private fun startWork(){
        val w = PeriodicWorkRequestBuilder<ActionWorker>(1,TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(w)
    }

    private fun startPushWork(){
        val w = PeriodicWorkRequestBuilder<ActionWorkerByPush>(1,TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(w)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventArrive(event: ActionEvent){
        Log.e("binghao", "onEventArrive: 收到消息 start work", )
        Toast.makeText(requireContext(), "收到消息，开始工作！", Toast.LENGTH_SHORT).show()
        startPushWork()
    }
}