package com.example.notiup

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import com.example.notiup.Alarm.AlarmFunctions
import com.example.notiup.databinding.BottomSheetBinding
import com.example.notiup.db.AlarmDao
import com.example.notiup.db.AppDatabase
import com.example.notiup.entity.Alarm
import com.example.notiup.viewModel.ScheduleModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


class BottomSheet(context : Context, fNumber : Int) : BottomSheetDialogFragment() {

    lateinit var binding : BottomSheetBinding
    private lateinit var spinner : Spinner
    private lateinit var adapter : CustomSpinnerAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var selectedDate : String
    private lateinit var alarmDao: AlarmDao
    private val fNumber = fNumber

    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }

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


    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the BottomSheetBehavior from the view
        val behavior = BottomSheetBehavior.from(view.parent as View)

        // Disable dragging behavior
        behavior.isDraggable = false

        behavior.peekHeight = 800

        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        // Disable peek height
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)

        val roomDb = AppDatabase.getInstance(requireContext())
        alarmDao = roomDb.alarmDao()

        binding = BottomSheetBinding.bind(view)
        db = Firebase.firestore

        val year = LocalDate.now().year.toString()
        val month = LocalDate.now().monthValue.toString()
        val day = LocalDate.now().dayOfMonth.toString()
        selectedDate = "$year-$month-$day"


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
            (activity as MainActivity).changeFragment(fNumber)
        }

        // 나경씨의 코드
        val list: ArrayList<DropdownList> = ArrayList()

        auth = Firebase.auth

        val a = DropdownList()
        a.setWord("aaaa")
        a.setImageRes(R.drawable.check_icon)
        list.add(a)

        val b = DropdownList()
        b.setWord("bbbbb")
        b.setImageRes(R.drawable.check_icon)
        list.add(b)

        val c = DropdownList()
        c.setWord("cccccc")
        c.setImageRes(R.drawable.check_icon)
        list.add(c)

        spinner = view.findViewById(R.id.custom_spinner)
        adapter = CustomSpinnerAdapter(requireContext(), list)
        spinner.adapter= adapter

        spinner.onItemSelectedListener

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                adapter.setSelectedItemPosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무 것도 선택되지 않았을 때 처리할 작업이 있다면 여기에 작성하세요.
            }
        }

        // 저장 버튼
        binding.btnSave.setOnClickListener {
            val currentUser = auth.currentUser
            if(currentUser == null) {   // 로그인 전
                uploadAlarm2()
            } else { // 로그인 시
                uploadAlarm()
            }
            if(binding.lockscreen.isChecked) {
                addAlarm(0)
                if(binding.banner.isChecked) addAlarm(2)
                else if(binding.noticenter.isChecked) addAlarm(1)
            } else {
                if(binding.banner.isChecked) addAlarm(2)
                else if(binding.noticenter.isChecked) addAlarm(1)
            }

            dismiss()
            (activity as MainActivity).changeFragment(fNumber)
        }

        return view
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

    private fun changeHour(hour: String): String {
        if(hour.length == 1) {
            return "0$hour"
        } else {
            return "$hour"
        }
    }

    private fun changeMinute(minute: String): String {
        if(minute.length == 1) {
            return "0$minute"
        } else {
            return "$minute"
        }
    }

    private fun uploadAlarm() { // 로그인 시 DB에 올리는 코드
        val currentUser = auth.currentUser
        var hour = changeHour(binding.startTimepicker.hour.toString())
        var minute = changeMinute(binding.startTimepicker.minute.toString())

        if(currentUser != null) {
            val schedule = ScheduleModel(
                binding.etTitle.text.toString(),
                binding.etMemo.text.toString(),
                selectedDate,
                "$hour : $minute"
            )

            db.collection("users").document(currentUser.email!!)
                .collection("schedule").add(schedule)
                .addOnSuccessListener {
                    Log.d("mytag", "DocumentSnapshot successfully written!")
                    Toast.makeText(mainActivity, "알람을 추가하였습니다.", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                .addOnFailureListener { e -> Log.w("mytag", "Error writing document", e) }
        } else {
            Toast.makeText(mainActivity, "알람을 추가하였습니다.", Toast.LENGTH_SHORT).show()
            dismiss()
        }

    }

    private fun uploadAlarm2() {    // 로그인 X시 DB에 올리는 코드
        val aTitle = binding.etTitle.text.toString()
        val sDate = "$selectedDate"
        val sHour = binding.startTimepicker.hour.toString()
        val sMinute = binding.startTimepicker.minute.toString()
        val scHour = changeHour(sHour)
        val scMinute = changeMinute(sMinute)
        val sTime = "$scHour : $scMinute" // 알람이 울리는 시간
        val eDate = "$selectedDate"
        val eHour = binding.startTimepicker.hour.toString()
        val eMinute = binding.startTimepicker.minute.toString()
        val ecHour = changeHour(eHour)
        val ecMinute = changeMinute(eMinute)
        val eTime = "$ecHour : $ecMinute"
        val repeat = binding.repeat
        val aMemo = binding.etMemo.text.toString()
//        val t_id_fk: Int

        val alarm = Alarm(
            atitle = aTitle,
            sdate = sDate,
            stime = sTime,
            edate = eDate,
            etime = eTime,
            repeat = 1,
            amemo = aMemo)
        CoroutineScope(Dispatchers.IO).launch{
            alarmDao.insert(alarm)
            Log.d("mytag", "insert 성공")
        }
    }

    private fun addAlarm(type : Int) {  // 휴대폰에 울릴 알람 추가하는 코드
        val text = binding.etTitle.text.toString()
        val content = binding.etMemo.text.toString()

        val hour = binding.startTimepicker.hour.toString()
        val minute = binding.startTimepicker.minute.toString()
        val time = "$selectedDate $hour:$minute:00" // 알람이 울리는 시간

        val random = (1..100000) // 1~100000 범위에서 알람코드 랜덤으로 생성 (추후 다른 방법으로 변경 필수!!겹칠 가능성이 존재함...)
        val alarmCode = random.random()
        setAlarm(alarmCode, content, text, time, type)
    }

    private fun setAlarm(alarmCode : Int, content : String, text : String, time : String, type : Int){
        alarmFunctions.callAlarm(time, alarmCode, text, content, type)
    }

}