package com.example.notiup

import android.app.KeyguardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.notiup.databinding.AlarmServiceBinding
import com.example.notiup.db.AlarmDao
import com.example.notiup.db.AppDatabase


class AlarmServiceActivity : AppCompatActivity() {
    private lateinit var binding: AlarmServiceBinding
    private lateinit var alarmDao: AlarmDao
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AlarmServiceBinding.inflate(layoutInflater)
        val view = binding.root
        val roomDb = AppDatabase.getInstance(this)
        alarmDao = roomDb.alarmDao()

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // 시간, 메모 가져오기
        val id = sharedPreferences.getInt("aId", 0)
        var stime = sharedPreferences.getString("sTime", "00 : 00")
        var amemo = sharedPreferences.getString("aMemo", "메모")


        binding.timeTv.text = stime
        binding.memoTv.text = amemo

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).apply {
                requestDismissKeyguard(this@AlarmServiceActivity, null)
            }
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        binding.reBtn.setOnClickListener {
            finish()
        }

        binding.stopBtn.setOnClickListener {
            finish()
        }

        // 잠금 화면에 Alarm 띄우기
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.alarm_service)


        setContentView(view)
    }

}