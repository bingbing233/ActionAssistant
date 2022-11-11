package com.example.actionassistant.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Base64
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
import com.example.actionassistant.utils.TAG
import com.example.actionassistant.utils.registerEvent
import com.example.actionassistant.utils.unregisterEvent
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.http2.Header
import okhttp3.internal.platform.Platform
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel:MainViewModel by activityViewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterEvent(this)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerEvent(this)
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
//        startPushWork()
    }

    private fun push(){
        val url = URL("https://api.jpush.cn/v3/push")
       val client = OkHttpClient()
        val kv = "7950077e6cd2c0a3a42009fa:80fedd71dd072f56aa9b195a " //appKey:masterSecret
        val author = Base64.decode(kv,Base64.DEFAULT).contentToString()
        val request = Request.Builder()
            .header("Authorization","Basic $author")
            .url(url)
            .build()
        val respond = client.newCall(request).execute()
        Log.e(TAG, "push: ${respond.message}", )

    }

    private fun base64(text:String):String{
        return Base64.decode(text,Base64.DEFAULT).contentToString()
    }
}