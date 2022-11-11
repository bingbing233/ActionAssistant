package com.example.actionassistant.broadcast

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.example.actionassistant.MyApp
import com.example.actionassistant.service.ActionWorker
import com.example.actionassistant.utils.ActionEvent
import com.example.actionassistant.utils.TAG
import com.example.actionassistant.utils.postEvent

class JPushReceiver : JPushMessageReceiver() {
    override fun getNotification(p0: Context?, p1: NotificationMessage?): Notification {
        Log.e(TAG, "getNotification: $p1", )
        return super.getNotification(p0, p1)
    }

    override fun onMessage(p0: Context?, p1: CustomMessage?) {
        super.onMessage(p0, p1)
        Log.e(TAG, "onMessage: ${p1?.message}", )
    }

    override fun onNotifyMessageOpened(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageOpened(p0, p1)
        Log.e(TAG, "onNotifyMessageOpened: $p1", )
    }

    //收到推送时调用
    override fun onNotifyMessageArrived(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageArrived(p0, p1)
        Log.e(TAG, "onNotifyMessageArrived: $p1", )
        postEvent(ActionEvent())
    }

    override fun onNotifyMessageUnShow(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageUnShow(p0, p1)
        Log.e(TAG, "onNotifyMessageUnShow: $p1", )
    }

    override fun onNotifyMessageDismiss(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageDismiss(p0, p1)
        Log.e(TAG, "onNotifyMessageDismiss: $p1", )
    }


}