package com.example.notiup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.notiup.databinding.CheckBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CheckBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding : CheckBottomSheetBinding

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

        val todayBtn = view.findViewById<RadioButton>(R.id.today)
        val allBtn = view.findViewById<RadioButton>(R.id.all)
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.today -> {
                    todayBtn.isChecked = true
                    allBtn.isChecked = false

                }
                R.id.all -> {
                    todayBtn.isChecked = false
                    allBtn.isChecked = true

                }
            }
        }

        binding.btnSave.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
}