package com.example.notiup

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.AlarmItemBinding
import com.example.notiup.viewModel.ScheduleModel
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    fun removeData(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }



    fun delete(position: Int) {
        Log.d("mytag", idList[position])
        db.collection("users").document(auth.currentUser!!.email!!)
            .collection("schedule").document(idList[position])
            .delete()
            .addOnSuccessListener {
                Log.d("mytag", "DocumentSnapsWhot successfully deleted!")
                notifyItemRemoved(position)
                idList.removeAt(position)
            }
            .addOnFailureListener { e -> Log.w("mytag", "Error deleting document", e) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList[position])

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int = R.layout.alarm_item
}