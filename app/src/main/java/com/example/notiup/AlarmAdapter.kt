package com.example.notiup

import Alarm
import AppDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.AlarmItemBinding
import com.example.notiup.databinding.ListItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent


class AlarmAdapter(val dataList : MutableList<ScheduleModel>) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule : ScheduleModel) {
            binding.tvTime.text = schedule.sTime
            binding.tvAlarmText.text = schedule.aMemo
            binding.tvDate.text = schedule.sDate
        }
    }

    private lateinit var db: AppDatabase

//    private val alarmList: ArrayList<Alarm> = arrayListOf<Alarm>().apply {
//        val alarmList = arrayListOf(
//            add(Alarm("08 : 30", "여기에 알람 문구가 뜹니다", "노랑")),
//            add(Alarm("07 : 30", "aaaaaa", "노랑")),
//            add(Alarm("12 : 20", "ddd", "노랑")),
//            add(Alarm("08 : 30", "여기에 알람 문구가 뜹니다", "노랑")),
//            add(Alarm("08 : 30", "여기에 알람 문구가 뜹니다", "노랑")),
//            add(Alarm("08 : 30", "여기에 알람 문구가 뜹니다", "노랑"))
//
//        )
//    }

    fun removeData(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    // 레이아웃과 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)  // 어댑터에 연결된 액티비티를 가져옴
        return ViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // dataSet의 사이즈 리턴
    override fun getItemCount(): Int = dataList.size

//    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val rv_alarm = itemView
//    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
    }

    fun removeAllItem() {
        dataList.clear()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList[position])

    override fun getItemViewType(position: Int): Int = R.layout.list_item

}