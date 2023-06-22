package com.example.notiup.bottomSheet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notiup.AlarmFragment
import com.example.notiup.MainActivity
import com.example.notiup.R
import com.example.notiup.databinding.CheckBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CheckBottomSheet(context : Context) : BottomSheetDialogFragment() {

    private lateinit var binding : CheckBottomSheetBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var mainActivity : MainActivity    // Activity 담긴 객체

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.check_bottom_sheet, container, false)

        binding = CheckBottomSheetBinding.bind(view)
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // 이전에 선택한 값을 가져와서 설정
        val savedType = sharedPreferences.getString("type", "all")
        val savedType2 = sharedPreferences.getString("type2", "time_asc")
        binding.radioGroup.check(if (savedType == "today") R.id.today else R.id.all)
        binding.radioGroup2.check(if (savedType2 == "time_desc") R.id.time_desc else R.id.time_asc)


        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.today -> {
                    Log.d("mytag", "today")
                    sharedPreferences.edit().putString("type", "today").apply()
                }
                R.id.all -> {
                    Log.d("mytag", "all")
                    sharedPreferences.edit().putString("type", "all").apply()
                }
            }
        }

        binding.radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.time_desc -> {
                    Log.d("mytag", "time_desc")
                    sharedPreferences.edit().putString("type2", "time_desc").apply()
                }
                R.id.time_asc -> {
                    Log.d("mytag", "time_asc")
                    sharedPreferences.edit().putString("type2", "time_asc").apply()
                }
            }
        }
        val fragment = AlarmFragment()
        binding.btnSave.setOnClickListener {
            val bundle = Bundle().apply {
                val type = sharedPreferences.getString("type", "all")
                val type2 = sharedPreferences.getString("type2", "time_asc")
                putString("type", type)
                putString("type2", type2)
            }
            fragment.arguments = bundle
            (activity as MainActivity).changeFragment(2)
            dismiss()
        }

        return view
    }

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
}