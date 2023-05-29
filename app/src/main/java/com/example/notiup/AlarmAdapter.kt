package com.example.notiup

import Alarm
import AppDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent


class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.CustomViewHolder>() {

    private lateinit var db: AppDatabase

    private val alarmList: ArrayList<Alarm> = arrayListOf<Alarm>().apply {
//        val alarmList = arrayListOf(
//            add(Alarm("08 : 30", "여기에 알람 문구가 뜹니다", "노랑")),
//            add(Alarm("07 : 30", "aaaaaa", "노랑")),
//            add(Alarm("12 : 20", "ddd", "노랑")),
//            add(Alarm("08 : 30", "여기에 알람 문구가 뜹니다", "노랑")),
//            add(Alarm("08 : 30", "여기에 알람 문구가 뜹니다", "노랑")),
//            add(Alarm("08 : 30", "여기에 알람 문구가 뜹니다", "노랑"))
//
//        )
    }

    fun removeData(position: Int) {
        alarmList.removeAt(position)
        notifyItemRemoved(position)
    }

    // 레이아웃과 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)  // 어댑터에 연결된 액티비티를 가져옴
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

    }

    // dataSet의 사이즈 리턴
    override fun getItemCount(): Int {
        return alarmList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rv_alarm = itemView
    }

    fun addItem(position: Int, item: Alarm) {
        alarmList.add(position, item)
    }

    fun removeItem(position: Int) {
        alarmList.removeAt(position)
    }

    fun removeAllItem() {
        alarmList.clear()
    }

}