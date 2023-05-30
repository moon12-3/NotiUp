package com.example.notiup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.notiup.Alarm.AlarmFunctions
import com.example.notiup.databinding.BottomSheetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

open class BottomSheetFragment : Fragment() {
    private lateinit var binding : BottomSheetBinding
    private lateinit var spinner : Spinner
    private lateinit var adapter : CustomSpinnerAdapter
    private lateinit var auth: FirebaseAuth

    private val alarmFunctions by lazy { AlarmFunctions(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)

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
