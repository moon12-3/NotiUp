package com.example.notiup

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.notiup.Alarm.AlarmFunctions
import com.example.notiup.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter

class BottomSheet(context : Context) : BottomSheetDialogFragment() {

    lateinit var binding : BottomSheetBinding
    private lateinit var spinner : Spinner
    private lateinit var adapter : CustomSpinnerAdapter
    private lateinit var auth: FirebaseAuth

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

        // 달력 꾸미기
        binding.startCal.apply {
            setWeekDayLabels(arrayOf("일", "월", "화", "수", "목", "금", "토"))    // 요일을 한글로 설정
            setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
            addDecorator(MonthFragment().WeekdayDecorator())
            setHeaderTextAppearance(R.style.CalendarWidgetHeader)
            setTopbarVisible(false)     // Topbar안보이게
        }

        binding.endCal.apply {
            setWeekDayLabels(arrayOf("일", "월", "화", "수", "목", "금", "토"))    // 요일을 한글로 설정
            setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
            addDecorator(MonthFragment().WeekdayDecorator())
            setHeaderTextAppearance(R.style.CalendarWidgetHeader)
            setTopbarVisible(false)     // Topbar안보이게
        }

        startDay.setOnClickListener {
            if(startCal.visibility==View.GONE) {
                startTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                startDay.setTextColor(Color.parseColor("#E7FE54"))
                startTimePicker.visibility = View.GONE
                endTimePicker.visibility = View.GONE
                endCal.visibility = View.GONE
                startCal.visibility = View.VISIBLE
                binding.endTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                binding.endDay.setTextColor(Color.parseColor("#ccFFFFFF"))
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
                startTimePicker.visibility = View.GONE
                startCal.visibility = View.GONE
                endCal.visibility = View.VISIBLE
                binding.startTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                binding.startDay.setTextColor(Color.parseColor("#ccFFFFFF"))
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
                endTimePicker.visibility = View.GONE
                endCal.visibility = View.GONE
                binding.endTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                binding.endDay.setTextColor(Color.parseColor("#ccFFFFFF"))
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
                startTimePicker.visibility = View.GONE
                startCal.visibility = View.GONE
                binding.startTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                binding.startDay.setTextColor(Color.parseColor("#ccFFFFFF"))
            }
            else {
                startTime.setTextColor(Color.parseColor("#ccFFFFFF"))
                startTimePicker.visibility = View.GONE
            }
        }

        // 취소 누르면 숨겨지게
        binding.cancel.setOnClickListener {
            dismiss()
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
            else addAlarm2() // 로그인 시
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
        val content = binding.etMemo.text.toString()

        val hour = binding.endTimepicker.hour.toString()
        val minute = binding.endTimepicker.minute.toString()
        val time = "2000-00-00 $hour:$minute:00" // 알람이 울리는 시간

        val random = (1..100000) // 1~100000 범위에서 알람코드 랜덤으로 생성 (추후 다른 방법으로 변경 필수!!겹칠 가능성이 존재함...)
        val alarmCode = random.random()
        setAlarm(alarmCode, content, time)
    }

    private fun setAlarm(alarmCode : Int, content : String, time : String){
        alarmFunctions.callAlarm(time, alarmCode, content)
    }
}