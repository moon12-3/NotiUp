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

class BottomSheet : Fragment() {

    private lateinit var spinner : Spinner
    private lateinit var adapter : CustomSpinnerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)

        val etMemo = view.findViewById<EditText>(R.id.et_memo)
        etMemo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etMemo.lineCount > 2) {
                    etMemo.text.delete(etMemo.selectionStart - 1, etMemo.selectionStart)
                }
                if (s?.toString()?.contains("\n") == true) {
                    etMemo.focusSearch(View.FOCUS_DOWN)?.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        etMemo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                etMemo.clearFocus()
                true
            } else {
                false
            }
        }

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
