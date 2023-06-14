package com.example.notiup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MonthCheckAdapter(val dataList : MutableList<String>): RecyclerView.Adapter<MonthCheckAdapter.ItemViewHolder>() {
    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return ItemViewHolder(view)
    }

    fun removeData(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val text = dataList[position]
    }

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int): Int = R.layout.check_item

}