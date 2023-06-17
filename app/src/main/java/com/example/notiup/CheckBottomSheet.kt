package com.example.notiup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return view
    }

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
}