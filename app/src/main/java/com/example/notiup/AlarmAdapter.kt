package com.example.notiup

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.ListItemBinding
import com.example.notiup.entity.Alarm
import com.example.notiup.viewModel.ScheduleModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AlarmAdapter(val dataList: MutableList<ScheduleModel>, val idList : MutableList<String>) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule : ScheduleModel) {
            binding.tvTime.text = schedule.sTime
            binding.tvAlarmText.text = schedule.aMemo
            binding.tvDate.text = schedule.sDate
        }
    }

    val db = Firebase.firestore
    val auth = Firebase.auth

    interface ItemClick{
        fun onClick(view: View, position: Int, list: ArrayList<Alarm>)
    }
    var itemClick : ItemClick? = null

    fun removeData(position: Int) {
        db.collection("users").document(auth.currentUser!!.email!!)
            .collection("schedule").document(idList[position])
            .delete()
            .addOnSuccessListener {
                Log.d("mytag", "DocumentSnapsWhot successfully deleted!")
                dataList.removeAt(position)
                notifyItemRemoved(position)
                idList.removeAt(position)
            }
            .addOnFailureListener { e -> Log.w("mytag", "Error deleting document", e) }
    }

    fun getItem(position: Int): ScheduleModel {
        return dataList[position]
    }

    // 레이아웃과 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // dataSet의 사이즈 리턴
    override fun getItemCount(): Int = dataList.size

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rv_alarm = itemView
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
    }

    fun removeAllItem() {
        dataList.clear()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList[position])

    override fun getItemViewType(position: Int): Int = R.layout.list_item

}