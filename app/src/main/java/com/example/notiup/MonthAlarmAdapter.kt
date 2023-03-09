package com.example.notiup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MonthAlarmAdapter(val dataList : MutableList<String>) : RecyclerView.Adapter<MonthAlarmAdapter.ItemViewHolder>() {
    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // 한 항목을 표시할 레이아웃 관련 뷰를 만들어 줌
        // (viewType값이 바로 getItemViewType에서 반환한 레이아웃 리소스 식별자)
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

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int = R.layout.alarm_item
}