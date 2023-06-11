package com.example.notiup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class CustomSpinnerAdapter(private val context: Context, private val items: ArrayList<DropdownList>) : BaseAdapter() {

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

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_dropdown_item, null)

        val textView = view.findViewById<TextView>(R.id.tvCustomSpinner)
        textView.text = items[position].getWord()

        val imageView = view.findViewById<ImageView>(R.id.ivCustomSpinner)
        imageView.setImageResource(items[position].getImageRes())

        return view
    }

}
