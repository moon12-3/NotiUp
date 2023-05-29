package com.example.notiup

import Alarm
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.CustomViewHolder>() {

    private lateinit var db: AppDataBase

    private val dataSet: ArrayList<Alarm> = arrayListOf<Alarm>().apply {
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
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)  // 어댑터에 연결된 액티비티를 가져옴
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        holder.time.text = dataSet.get(position).time // TODO: 시간으로 받으면 .toString() 써주기
//        holder.text.text = dataSet.get(position).text
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.findViewById<TextView>(R.id.tv_time)        // 시간
        val text = itemView.findViewById<TextView>(R.id.tv_alarm_text)  // 알람 문구
        val switch = itemView.findViewById<Switch>(R.id.alarm_switch)   // on/off
    }

    fun addItem(position: Int, item: Alarm) {
        dataSet.add(position, item)
    }

    fun removeItem(position: Int) {
        dataSet.removeAt(position)
    }

    fun removeAllItem() {
        dataSet.clear()
    }

}