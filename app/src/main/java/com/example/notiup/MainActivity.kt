package com.example.notiup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notiup.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.frame, MonthFragment()).commit()

        val navView2 = findViewById<BottomNavigationView>(R.id.navigationView)

        navView2.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.article_month -> supportFragmentManager.beginTransaction().replace(R.id.frame, MonthFragment()).commit()
                R.id.article_alarm -> supportFragmentManager.beginTransaction().replace(R.id.frame, AlarmFragment()).commit()
            }

            true
        }
    }
}