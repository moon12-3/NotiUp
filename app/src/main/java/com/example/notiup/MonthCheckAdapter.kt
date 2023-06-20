package com.example.notiup

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.AlarmItemBinding
import com.example.notiup.databinding.CheckItemBinding
import com.example.notiup.viewModel.ScheduleModel
import com.example.notiup.viewModel.TodoModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MonthCheckAdapter(val dataList : MutableList<TodoModel>, val idList : MutableList<String>): RecyclerView.Adapter<MonthCheckAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val binding : CheckItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo : TodoModel) {
            binding.vocaName.text = todo.name
        }
    }

    val db = Firebase.firestore
    val auth = Firebase.auth

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(CheckItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(dataList[position])

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int): Int = R.layout.check_item

}