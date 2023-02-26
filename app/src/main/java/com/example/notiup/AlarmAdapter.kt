package com.example.notiup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlarmAdapter(val AlarmList: ArrayList<Alarms>) : RecyclerView.Adapter<AlarmAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)  // 어댑터에 연결된 액티비티를 가져옴
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.time.text = AlarmList.get(position).time // TODO: 시간으로 받으면 .toString() 써주기
        holder.text.text = AlarmList.get(position).text
    }

    override fun getItemCount(): Int {
        return AlarmList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.findViewById<TextView>(R.id.tv_time)        // 시간
        val text = itemView.findViewById<TextView>(R.id.tv_alarm_text)  // 알람 문구
        val switch = itemView.findViewById<Switch>(R.id.alarm_switch)   // on/off
    }

}