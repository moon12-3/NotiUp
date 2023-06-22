package com.example.notiup

import android.content.Context
import android.content.Intent.getIntent
import android.content.SharedPreferences
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.FragmentAlarmBinding
import com.example.notiup.db.AlarmDao
import com.example.notiup.db.AppDatabase
import com.example.notiup.entity.Alarm
import com.example.notiup.viewModel.ScheduleModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class AlarmFragment : Fragment() {

    lateinit var mainActivity : MainActivity
    lateinit var binding : FragmentAlarmBinding
    private lateinit var rvAdapter : AlarmAdapter
    private lateinit var rvAdapter2 : AlarmAdapter2
    private lateinit var recyclerView : RecyclerView
    private lateinit var alarm: LiveData<MutableList<Alarm>>
    private lateinit var auth: FirebaseAuth // 로그인
    lateinit var db : FirebaseFirestore // DB
    private lateinit var alarmDao: AlarmDao
    private lateinit var sharedPreferences: SharedPreferences


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)

        binding = FragmentAlarmBinding.bind(view)
        val roomDb = AppDatabase.getInstance(requireContext())
        alarmDao = roomDb.alarmDao()

//        val bundle = arguments ?: Bundle().apply {
//            putString("type", "all")
//            putString("type2", "time_asc")
//        }
//        val type = bundle.getString("type", "all")
//        val type2 = bundle.getString("type2", "time_asc")

        // 라디오 버튼 값 받아오기
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedType = sharedPreferences.getString("type", "all")
        val savedType2 = sharedPreferences.getString("type2", "time_asc")

        // bottom view
        // CHECK BOX bottom view
        val checkBottomSheetView = layoutInflater.inflate(R.layout.check_bottom_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(mainActivity, R.style.BottomSheetDialogTheme)

        // 알람 추가 bottom View
        bottomSheetDialog.setContentView(checkBottomSheetView)
        binding.fabEdit.setOnClickListener {
            val today = SimpleDateFormat("yyyy-M-d")
            val todayText = today.format(Date())
            setFragmentResult("requestKey", bundleOf("bundleKey" to todayText))
            val bottomSheet = BottomSheet(mainActivity, 2)
            bottomSheet.show(mainActivity.getSupportFragmentMana(), bottomSheet.tag)
        }
        // 알람 필터 bottom View
        binding.fabFilter.setOnClickListener {
            val bottomSheet = CheckBottomSheet()
            bottomSheet.show(mainActivity.getSupportFragmentMana(), bottomSheet.tag)
        }

        //recyclerView 내용 (알람)
        recyclerView = view.findViewById(R.id.rv_alarm)

        // 로그인 했는지 관련
        if(auth.currentUser != null) { // 로그인 했을 시
            setDB2()
        } else {
            setDB(savedType!!, savedType2!!)
        }


        // ItemTouchHelper의 callback 함수
        val itemCallback = object : ItemTouchHelper.SimpleCallback (
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(auth.currentUser != null) { // 로그인 했을 시
                    rvAdapter.removeData(viewHolder.layoutPosition)
                } else {
                    val position = viewHolder.adapterPosition
                    val alarmToDelete: Alarm = rvAdapter2.getItem(position)

                    lifecycleScope.launch(Dispatchers.IO) {
                        alarmDao.delete(alarmToDelete)
                        // 해당 위치의 데이터 삭제
                        rvAdapter2.removeData(viewHolder.layoutPosition)
                    }
                }

            }


            // 꾹 눌러 이동할 수 없도록 함
            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val itemHeight = itemView.height

                    val textPadding = 40f // 텍스트 패딩 값

                    if (dX < 0) {   // 스와이프를 왼쪽으로 할 수록 -dX

                        // Draw the background
                        val backgroundPaint = Paint()
                        backgroundPaint.color = Color.parseColor("#CF6262")
                        val backgroundLeft = itemView.right + dX.toInt()
                        val backgroundTop = itemView.top
                        val backgroundRight = itemView.right
                        val backgroundBottom = itemView.bottom
                        c.drawRect(backgroundLeft.toFloat(), backgroundTop.toFloat(), backgroundRight.toFloat(), backgroundBottom.toFloat(), backgroundPaint)

                        // Draw the text
                        val text = "삭제"
                        val textPaint = Paint()
                        textPaint.color = Color.WHITE
                        textPaint.textSize = 52f
                        val textWidth = textPaint.measureText(text)
//                        val textX = dX + textPadding
                        val textX = (itemView.right - textWidth - 40)
                        val textY = (itemView.top + itemHeight / 2 + textPaint.textSize / 2 - 8).toFloat()
                        c.drawText(text, textX, textY, textPaint)

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

//        with(recyclerView) {
//            // 레이아웃매니저 설정
//            layoutManager = LinearLayoutManager(context)
//            setHasFixedSize(true)
//            // 어댑터 설정
//            adapter = rvAdapter
//
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        }

        // 리사이클러뷰에 ItemTouchHelper 적용
        ItemTouchHelper(itemCallback).attachToRecyclerView(recyclerView)
        return view
    }

    // DB에서 추가한 알람 불러오는 함수
    fun setDB(sortType: String, sortType2: String) {
        val today = SimpleDateFormat("yyyy-M-dd", Locale.getDefault()).format(Date())
        val sortedAlarms: LiveData<MutableList<Alarm>>

        if (sortType == "today") {  // 오늘 알람
            sortedAlarms = if (sortType2 == "time_asc") {
                alarmDao.getTodayAlarmSortedBySdate(today)
            } else {
                alarmDao.getTodayAlarmSortedBySdateDesc(today)
            }
        } else {    // 전체 알람
            sortedAlarms = if (sortType2 == "time_asc") {
                alarmDao.getAllAlarmSortedBySdate()
            } else {
                alarmDao.getAllAlarmSortedBySdateDesc()
            }
        }

        sortedAlarms.observe(viewLifecycleOwner, androidx.lifecycle.Observer { alarms ->
            // rvAdapter2 초기화
            rvAdapter2 = AlarmAdapter2(alarms)

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = rvAdapter2
            recyclerView.setHasFixedSize(true)
        })
    }

    private fun setDB2() {
        val docRef = db.collection("users").document(auth.currentUser!!.email!!)
            .collection("schedule").orderBy("sdate") // 최근 알람 순(금방 울릴 알람부터)

        docRef.get()
            .addOnSuccessListener { result ->
                val scheduleList = mutableListOf<ScheduleModel>()
                val idList = mutableListOf<String>()
                for (document in result) {
                    val schedule = document.toObject<ScheduleModel>()
                    scheduleList.add(schedule)
                    idList.add(document.id)
                    Log.d("mytag", "${document.id} => ${document.data}")
                }

                rvAdapter = AlarmAdapter(scheduleList, idList)

                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = rvAdapter
                recyclerView.setHasFixedSize(true)
            }
    }
}