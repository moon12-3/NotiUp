package com.example.notiup.Alarm.Receiver

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.notiup.Alarm.Service.AlarmService
import com.example.notiup.AlarmServiceActivity
import com.example.notiup.R

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var manager: NotificationManager
    private lateinit var builder: NotificationCompat.Builder

    // 채널 설정
    companion object{
        const val CHANNEL_ID = "banner"
        const val CHANNEL_ID2 = "noticenter"
        const val CHANNEL_NAME = "notiupchannel1"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent2 = Intent(context, AlarmService::class.java)
        val requestCode = intent?.extras!!.getInt("alarm_rqCode")
        val title = intent.extras!!.getString("text")
        val content = intent.extras!!.getString("content")
        val type = intent.extras!!.getInt("type")

//        val important = if(type==2) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT // HIGH면 headup

        if (type == 0) {
            val activityIntent = Intent(context, AlarmServiceActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            context.startActivity(activityIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.startForegroundService(activityIntent)
            } else {
                context?.startService(activityIntent)
            }
            return
        }

        // 채널 생성 및 builder 초기화
        if (type == 2) {
            Log.d("mytag", "배너 체크 O type : $type")
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
            manager.createNotificationChannel(channel)
        } else if (type == 1) {
            Log.d("mytag", "배너 체크 X type : $type")
            val channel = NotificationChannel(
                CHANNEL_ID2,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder = NotificationCompat.Builder(context, CHANNEL_ID2)
            manager.createNotificationChannel(channel)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            PendingIntent.getActivity(context,requestCode,intent2,PendingIntent.FLAG_IMMUTABLE); //Activity를 시작하는 인텐트 생성
        }else {
            PendingIntent.getActivity(context,requestCode,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if(type==2) {
            builder.setFullScreenIntent(pendingIntent, true) // FullScreen을 통해 해당 작업을 확인해야 다른 작업이 가능하도록 함
        }

        val notification = if(type==2)
            builder.setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // headup일때 필요
                .setContentText(content)
                .setSmallIcon(R.drawable.alarm)
                .setAutoCancel(true)
//                .setTimeoutAfter(3000) // 지정한 시간 이후 알림 취소(계산해서 넘기자)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build()
        else
            builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.alarm)
                .setAutoCancel(true)
//            .setTimeoutAfter() 지정한 시간 이후 알림 취소(계산해서 넘기자)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build()

        if(type==2) manager.notify(requestCode, notification) // 실행
        else manager.notify(requestCode, notification)
    }
}