package com.example.notiup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notiup.databinding.CheckBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CheckBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding : CheckBottomSheetBinding
    private val PREF_CHECKED_ID = "pref_checked_id"
    private val PREF_CHECKED_ID2 = "pref_checked_id2"

    // SharedPreferences 초기화
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("mypref", Context.MODE_PRIVATE)
    }

    private fun setInitialCheckedId() {
        val checkedId = loadCheckedId(1) // 첫 번째 라디오 그룹의 체크한 값을 불러옴
        binding.radioGroup.check(checkedId)
        val checkedId2 = loadCheckedId(2) // 두 번째 라디오 그룹의 체크한 값을 불러옴
        binding.radioGroup2.check(checkedId2)
    }

    // 저장된 체크한 ID 불러오기
    private fun loadCheckedId(group: Int): Int {
        val prefKey = when (group) {
            1 -> PREF_CHECKED_ID    // 라디오 그룹 1
            2 -> PREF_CHECKED_ID2   // 라디오 그룹 2
            else -> PREF_CHECKED_ID
        }
        return sharedPreferences.getInt(prefKey, 0)
    }

    private fun saveCheckedId(group: Int, checkedId: Int) {
        val prefKey = when (group) {
            1 -> PREF_CHECKED_ID    // 라디오 그룹 1
            2 -> PREF_CHECKED_ID2   // 라디오 그룹 2
            else -> PREF_CHECKED_ID
        }
        sharedPreferences.edit().putInt(prefKey, checkedId).apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.check_bottom_sheet, container, false)

        binding = CheckBottomSheetBinding.bind(view)
        binding.cancel.setOnClickListener {
            dismiss()
        }

        val bundle = Bundle()

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.today -> {
                    Log.d("mytag", "today")
                    bundle.putInt("type", 0)
                }
                R.id.all -> {
                    Log.d("mytag", "all")
                    bundle.putInt("type", 1)
                }
            }
        }

        binding.radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.time_desc -> {
                    Log.d("mytag", "time_desc")
                    bundle.putInt("type2", 0)
                }
                R.id.time_asc -> {
                    Log.d("mytag", "time_asc")
                    bundle.putInt("type2", 1)
                }
            }
        }

        val fragment = AlarmFragment()
        fragment.arguments = bundle

        binding.btnSave.setOnClickListener {
            val checkedId = binding.radioGroup.checkedRadioButtonId
            val checkedId2 = binding.radioGroup2.checkedRadioButtonId

            // 체크한 ID 값을 저장
            saveCheckedId(1, checkedId)
            saveCheckedId(2, checkedId2)

            dismiss()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialCheckedId()
    }

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
}