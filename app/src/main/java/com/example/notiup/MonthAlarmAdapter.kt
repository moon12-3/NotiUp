package com.example.notiup

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.AlarmItemBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MonthAlarmAdapter(val dataList : MutableList<ScheduleModel>, val idList : MutableList<String>) : RecyclerView.Adapter<MonthAlarmAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding : AlarmItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule : ScheduleModel) {
            binding.alarmName.text = schedule.aName
        }
    }

    val db = Firebase.firestore
    val auth = Firebase.auth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 한 항목을 표시할 레이아웃 관련 뷰를 만들어 줌
        // (viewType값이 바로 getItemViewType에서 반환한 레이아웃 리소스 식별자)
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return ViewHolder(AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun removeData(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun delete(position: Int) {
        val coll = "schedule ${auth.currentUser!!.email}"

        db.collection(coll).document(idList[position])
            .delete()
            .addOnSuccessListener {
                Log.d("mytag", "DocumentSnapsWhot successfully deleted!")
                idList.removeAt(position)
            }
            .addOnFailureListener { e -> Log.w("mytag", "Error deleting document", e) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList[position])

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int = R.layout.alarm_item
}