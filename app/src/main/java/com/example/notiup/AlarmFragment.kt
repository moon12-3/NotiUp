package com.example.notiup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.FragmentAlarmBinding

class AlarmFragment : Fragment() {

    lateinit var binding : FragmentAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)
        val alarmList = arrayListOf(
            Alarms("08 : 30", "여기에 알람 문구가 뜹니다", "노랑"),
            Alarms("07 : 30", "aaaaaa", "노랑"),
            Alarms("12 : 20", "ddd", "노랑"),
            Alarms("08 : 30", "여기에 알람 문구가 뜹니다", "노랑"),
            Alarms("08 : 30", "여기에 알람 문구가 뜹니다", "노랑"),
            Alarms("08 : 30", "여기에 알람 문구가 뜹니다", "노랑")

        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_alarm)

        // 레이아웃매니저 설정
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        // 어댑터 설정
        recyclerView.adapter = AlarmAdapter(alarmList)

        return view
    }
}