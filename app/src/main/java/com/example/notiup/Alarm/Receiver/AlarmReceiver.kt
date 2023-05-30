package com.example.notiup.Alarm.Receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.notiup.Alarm.Service.AlarmService

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var manager: NotificationManager
    private lateinit var builder: NotificationCompat.Builder

    // 채널 설정
    companion object{
        const val CHANNEL_ID = "channel"
        const val CHANNEL_NAME = "channel1"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //NotificationChannel 인스턴스를 createNotificationChannel()에 전달하여 앱 알림 채널을 시스템에 등록
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        builder = NotificationCompat.Builder(context, CHANNEL_ID)

        val intent2 = Intent(context, AlarmService::class.java)
        

    }
}