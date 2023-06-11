package com.example.notiup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast


class CustomSpinnerAdapter(private val context: Context, private val items: ArrayList<DropdownList>) : BaseAdapter() {

    private var selectedItemPosition: Int = -1

    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_dropdown_item, null)

        val textView = view.findViewById<TextView>(R.id.tvCustomSpinner)
        textView.text = items[position].getWord()

        val imageView = view.findViewById<ImageView>(R.id.ivCustomSpinner)
        imageView.setImageResource(items[position].getImageRes())

        view.setBackgroundResource(R.drawable.right_arrow)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_dropdown_item, null)

        val textView = view.findViewById<TextView>(R.id.tvCustomSpinner)
        textView.text = items[position].getWord()

        val imageView = view.findViewById<ImageView>(R.id.ivCustomSpinner)
//        imageView.setImageResource(items[position].getImageRes())
//
//        items[position].setImageRes(R.drawable.check_icon)

        if (position == selectedItemPosition) {
            imageView.visibility = View.VISIBLE // 다른 아이템의 이미지를 표시

        } else {
            imageView.visibility = View.INVISIBLE // 선택된 아이템의 이미지를 숨김
            imageView.setImageResource(items[position].getImageRes())
        }

        return view
    }

}
