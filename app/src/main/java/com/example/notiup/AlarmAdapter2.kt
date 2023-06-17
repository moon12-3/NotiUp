package com.example.notiup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.ListItemBinding
import com.example.notiup.db.AlarmDao
import com.example.notiup.entity.Alarm


class AlarmAdapter2(val dataList: MutableList<Alarm>) : RecyclerView.Adapter<AlarmAdapter2.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.tvTime.text = alarm.stime
            binding.tvAlarmText.text = alarm.amemo
            binding.tvDate.text = alarm.sdate
        }
    }

    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<Alarm>)
    }
    var itemClick : ItemClick? = null

    fun removeData(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Alarm {
        return dataList[position]
    }

    // 레이아웃과 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = dataList[position]
        val binding = ListItemBinding.bind(holder.itemView)  // ViewHolder에 바인딩 객체 전달
        val viewHolder = ViewHolder(binding)
        viewHolder.bind(alarm)
    }

    // dataSet의 사이즈 리턴
    override fun getItemCount(): Int = dataList.size

    inner class CustomViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val rv_alarm = binding.root
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
    }

    fun removeAllItem() {
        dataList.clear()
    }

    override fun getItemViewType(position: Int): Int = R.layout.list_item

}