package com.example.notiup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.notiup.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*
import kotlin.collections.ArrayList

class BottomSheet : Fragment() {
    private lateinit var binding : BottomSheetBinding
    private lateinit var spinner : Spinner
    private lateinit var adapter : CustomSpinnerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)

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
            addAlarm()
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

}
