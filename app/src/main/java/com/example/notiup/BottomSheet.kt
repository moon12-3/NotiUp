package com.example.notiup

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.notiup.Alarm.AlarmFunctions
import com.example.notiup.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class BottomSheet(context : Context) : BottomSheetDialogFragment() {

    lateinit var binding : BottomSheetBinding
    private lateinit var spinner : Spinner
    private lateinit var adapter : CustomSpinnerAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var selectedDate : String

    private lateinit var mainActivity : MainActivity    // Activity 담긴 객체

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onStart() {
        super.onStart()

        setFragmentResultListener("requestKey") { key, bundle ->
            selectedDate = bundle.getString("bundleKey")!!
            Log.d("test", "선택된 날짜 : " + selectedDate)

            val date = selectedDate.split("-")

            val day = "${date[0]}. ${date[1]}. ${date[2]}."

            binding.startDay.text = day
            binding.endDay.text = day

//            val dates = date.map { it.toInt() }.toTypedArray()
//
//            binding.startCal.selectedDate = CalendarDay.from(dates[0], dates[1], dates[2]) 달력에 표시가 안됨...추후 해결 필요
        }
    }

    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)

        binding = BottomSheetBinding.bind(view)
        db = Firebase.firestore

        setSetting() // 날짜 및 시간 visible 변경 관련 설정

        binding.startCal.setOnDateChangedListener { _, date, _ ->
            var year = date.year
            var month = date.month + 1
            var day = date.day
            selectedDate = "$year-$month-$day"

            val eday = "$year. $month. $day."

            binding.startDay.text = eday
        }

        binding.endCal.setOnDateChangedListener { _, date, _ ->
            var year = date.year
            var month = date.month + 1
            var day = date.day
            val eday = "$year $month. $day."

            binding.endDay.text = eday
        }

        // 취소 누르면 숨겨지게
        binding.cancel.setOnClickListener {
            dismiss()
            (activity as MainActivity).changeFragment(1)
        }

        // 나경씨의 코드
        val list: ArrayList<DropdownList> = ArrayList()

        binding = BottomSheetBinding.bind(view)
        auth = Firebase.auth

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
            val currentUser = auth.currentUser

            if(currentUser == null) addAlarm()  // 로그인 전
            else {
                addAlarm2()
                uploadAlarm()
            } // 로그인 시
        }

//        spinner = view.findViewById(R.id.custom_spinner)
//
//        adapter = CustomSpinnerAdapter(requireContext(), list)
//        spinner.adapter = adapter

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
//        val user_id: Int
//        val tag_id: Int
    }

    private fun addAlarm2() {
        val text = binding.etTitle.text.toString()
        val content = binding.etMemo.text.toString()

        val hour = binding.startTimepicker.hour.toString()
        val minute = binding.startTimepicker.minute.toString()
        val time = "$selectedDate $hour:$minute:00" // 알람이 울리는 시간

        val random = (1..100000) // 1~100000 범위에서 알람코드 랜덤으로 생성 (추후 다른 방법으로 변경 필수!!겹칠 가능성이 존재함...)
        val alarmCode = random.random()
        setAlarm(alarmCode, content, text, time)
    }

    private fun uploadAlarm() {
        var hour = binding.startTimepicker.hour.toString()
        var minute = binding.startTimepicker.minute.toString()
        if(hour.length == 1) hour = "0$hour"
        if(minute.length == 1) minute = "0$minute"
        val schedule = ScheduleModel(
            binding.etTitle.text.toString(),
            binding.etMemo.text.toString(),
            selectedDate,
            "$hour : $minute"
        )

        val coll = "schedule ${auth.currentUser!!.email}"
        db.collection(coll).add(schedule)
            .addOnSuccessListener {
                Log.d("mytag", "DocumentSnapshot successfully written!")
                Toast.makeText(mainActivity, "알람을 추가하였습니다.", Toast.LENGTH_SHORT).show()
                dismiss()
                (activity as MainActivity).changeFragment(1)
            }
            .addOnFailureListener { e -> Log.w("mytag", "Error writing document", e) }
    }

    private fun setAlarm(alarmCode : Int, content : String, text : String, time : String){
        alarmFunctions.callAlarm(time, alarmCode, text, content)
    }

    private fun setSetting() {
        // 시작 날짜 정하기
        val startDay = binding.startDay
        val startCal = binding.startCal

        // 끝나는 날짜 정하기
        val endDay = binding.endDay
        val endCal = binding.endCal

        // 시작 시간
        val startTime = binding.startTime
        val endTime = binding.endTime
        // 시계
        val startTimePicker = binding.startTimepicker
        startTimePicker.setIs24HourView(true)
        val endTimePicker = binding.endTimepicker
        endTimePicker.setIs24HourView(true)

        startDay.setOnClickListener {
            if(startCal.visibility==View.GONE) {
                startTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                startDay.setTextColor(Color.parseColor("#E7FE54"))
                startTimePicker.visibility = View.GONE
                startCal.visibility = View.VISIBLE
            }
            else {
                startDay.setTextColor(Color.parseColor("#ccFFFFFF"))
                startCal.visibility = View.GONE
            }
        }

        endDay.setOnClickListener {
            if(endCal.visibility==View.GONE) {
                endTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                endDay.setTextColor(Color.parseColor("#E7FE54"))
                endTimePicker.visibility = View.GONE
                endCal.visibility = View.VISIBLE
            }
            else {
                endDay.setTextColor(Color.parseColor("#ccFFFFFF"))
                endCal.visibility = View.GONE
            }
        }

        startTime.setOnClickListener {
            if(startTimePicker.visibility == View.GONE) {
                startTime.setTextColor(Color.parseColor("#E7FE54"))
                startDay.setTextColor(Color.parseColor("#ccFFFFFF"))
                startTimePicker.visibility = View.VISIBLE
                startCal.visibility = View.GONE
            }
            else {
                startTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                startTimePicker.visibility = View.GONE
            }
        }

        endTime.setOnClickListener {
            if(endTimePicker.visibility == View.GONE) {
                endTime.setTextColor(Color.parseColor("#E7FE54"))
                endDay.setTextColor(Color.parseColor("#ccFFFFFF"))
                endTimePicker.visibility = View.VISIBLE
                endCal.visibility = View.GONE
            }
            else {
                startTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                startTimePicker.visibility = View.GONE
            }
        }
    }
}