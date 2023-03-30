package com.example.notiup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment

class BottomSheet : Fragment() {

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

        spinner = view.findViewById(R.id.custom_spinner)

        adapter = CustomSpinnerAdapter(requireContext(), list)
        spinner.adapter = adapter

        return view
    }

}
