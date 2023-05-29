package com.example.notiup

import Alarm
import AlarmDao
import AppDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.notiup.databinding.BottomSheetBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class BottomSheet : Fragment() {
    private lateinit var binding : BottomSheetBinding
    private lateinit var spinner : Spinner
    private lateinit var adapter : CustomSpinnerAdapter
    private lateinit var mAlarmDao : AlarmDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)
        // FirebaseAuth 인스턴스 가져오기
        val auth = FirebaseAuth.getInstance()

        // 현재 인증된 사용자의 UID 가져오기
        val uid = auth.currentUser?.uid

        // 데이터베이스 인스턴스 초기화
        val db = AppDatabase.getDatabase(requireContext())!!

        mAlarmDao = db.alarmDao()   // 인터페이스 객체 할당

        // 값을 저장할 Alarm 객체 생성
        val currentMillis = LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toInstant()?.toEpochMilli() ?: 0
        val alarm = Alarm(aname = "알람 이름", atext = "알람 소개", sday = currentMillis.toLong(), stime = currentMillis.toLong(), eday = currentMillis.toLong(), etime = currentMillis.toLong(), repeat = 1, amemo = "메모", lockscreen = true, noticenter = true, banner = false, t_id_fk = 1)

        // 값 저장
        mAlarmDao.insert(alarm)
        Log.d("mytag", "insert 성공!")

        // dropdown
        val list: ArrayList<DropdownList> = ArrayList()

        val a = DropdownList()
        a.setWord("aaaa")
        a.setImageRes(R.drawable.check_icon)
        list.add(a)

        val b = DropdownList()
        a.setWord("bbbbb")
        a.setImageRes(R.drawable.check_icon)
        list.add(b)

        val c = DropdownList()
        a.setWord("cccccc")
        a.setImageRes(R.drawable.check_icon)
        list.add(c)

        binding.btnSave.setOnClickListener {
//            addAlarm()
            // TODO: if문 넣기(모든 항목에 값이 있는 경우에 실행)
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
        }

        spinner = view.findViewById(R.id.custom_spinner)

        adapter = CustomSpinnerAdapter(requireContext(), list)
        spinner.adapter = adapter

        return view
    }

    private fun addAlarm() {
        val aname = binding.etTitle.text
        val sday = binding.startDay
        val stime = binding.startTime
        val eday = binding.endDay
        val etime = binding.endTime
        val repeat = binding.repeat
        val amemo = binding.etMemo
        val lockscreen = binding.lockscreen
        val noticenter = binding.noticenter
        val banner = binding.banner
//        val tag_id: Int
    }

}
