package com.example.notiup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
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
                R.id.article_my -> supportFragmentManager.beginTransaction().replace(R.id.frame, UserFragment()).commit()
            }
            true
        }
    }

    fun changeFragment(index:Int) {
        when(index) {
            1-> {
                supportFragmentManager.beginTransaction().replace(R.id.frame, MonthFragment()).commit()
            }
        }
    }
}