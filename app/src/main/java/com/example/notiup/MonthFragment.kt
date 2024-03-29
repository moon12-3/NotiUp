package com.example.notiup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.bottomSheet.BottomSheet
import com.example.notiup.databinding.FragmentMonthBinding
import com.example.notiup.viewModel.ScheduleModel
import com.example.notiup.viewModel.TodoModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment : Fragment() {

    private lateinit var mainActivity : MainActivity    // Activity 담긴 객체
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var binding : FragmentMonthBinding // binding 용

    private lateinit var selectedDate: String // 달력에서 선택한 날짜

    private lateinit var recyclerView : RecyclerView
    private lateinit var cRecyclerView : RecyclerView
    private lateinit var rvAdapter : MonthAlarmAdapter
    private lateinit var cAdapter : MonthCheckAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    var today = SimpleDateFormat("yyyy-MM-dd")
    var now = SimpleDateFormat("HH : mm")
    var todayText: String = today.format(Date())
    var nowText: String = now.format(Date())

    private lateinit var mBR : BroadcastReceiver    // 매 분마다 호출(끝나는 시간이 지나면 알람 삭제, 체크리스트 추가)

    override fun onStart() {
        super.onStart()
        mBR = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                if (intent.action?.compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    today = SimpleDateFormat("yyyy-MM-dd")
                    now = SimpleDateFormat("HH : mm")
                    todayText = today.format(Date())
                    nowText = now.format(Date())
                    Log.d("mytag", "현재 날짜 : $todayText, 현재 시간 : $nowText")
                    setDB()
                    setListDB()
                }
            }
        }
        registerReceiver(mainActivity, mBR, IntentFilter(Intent.ACTION_TIME_TICK), RECEIVER_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
        mBR?.let { mainActivity.unregisterReceiver(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_month, container, false)

        binding = FragmentMonthBinding.bind(view)
        db = Firebase.firestore
        auth = Firebase.auth

        binding.materialCalendar.selectedDate = CalendarDay.today()

        // 현재 날짜
        var year = binding.materialCalendar.selectedDate!!.year
        var month = binding.materialCalendar.selectedDate!!.month + 1
        var monthText = month.toString()
        if(monthText.length==1) monthText = "0${monthText}"
        var day = binding.materialCalendar.selectedDate!!.day
        var dayText = day.toString()
        if(dayText.length==1) dayText = "0${dayText}"
        selectedDate = "$year-$monthText-$dayText"
        binding.calHeader.text = "${year}년 ${month}월"

        //recyclerView 내용 (알람)
        recyclerView = view.findViewById(R.id.fragment_container)
        cRecyclerView = binding.checklistContainer

        // DB 가져오기
        setDB()
        setListDB()

        binding.materialCalendar.apply {
            setWeekDayLabels(arrayOf("일", "월", "화", "수", "목", "금", "토"))    // 요일을 한글로 설정
            setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
            addDecorator(WeekdayDecorator())
            setHeaderTextAppearance(R.style.CalendarWidgetHeader)
            setTopbarVisible(false)     // Topbar안보이게
        }

        if(auth.currentUser !=null) dotDecorator()  // 로그인 시


        // 달력 날짜 선택 Listener
        binding.materialCalendar.setOnDateChangedListener { _, date, _ ->
            var year = date.year
            var month = date.month + 1
            var monthText = month.toString()
            if(monthText.length==1) monthText = "0${monthText}"
            var day = date.day
            var dayText = day.toString()
            if(dayText.length==1) dayText = "0${dayText}"
            selectedDate = "$year-$monthText-$dayText"
            Log.d("mytag", selectedDate)

            setDB()
            setListDB()
        }

        binding.materialCalendar.setOnMonthChangedListener { _, date ->
            var year = date.year
            var month = date.month + 1
            binding.calHeader.text = "${year}년 ${month}월"
        }

        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .build()


        // bottom sheet 내용
        val tagBottomSheetView = layoutInflater.inflate(R.layout.tag_bottom_sheet, null)

        val tagBottomSheetDialog = BottomSheetDialog(mainActivity, R.style.BottomSheetDialogTheme)

        tagBottomSheetDialog.setContentView(tagBottomSheetView)


        binding.fabEdit.setOnClickListener {
            setFragmentResult("requestKey", bundleOf("bundleKey" to selectedDate))
            val bottomSheet = BottomSheet(mainActivity, 1)
            bottomSheet.setCancelable(false)
            bottomSheet.show(mainActivity.getSupportFragmentMana(), bottomSheet.tag)
        }

        binding.fabTag.setOnClickListener {
            Toast.makeText(mainActivity, "COMING SOON...", Toast.LENGTH_SHORT).show()
        }

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
                // 해당 위치의 데이터 삭제
                if(auth.currentUser != null) {
                    rvAdapter.removeData(viewHolder.layoutPosition)
                    rvAdapter.delete(viewHolder.layoutPosition)
                }
            }

            // 꾹 눌러 이동할 수 없도록 함
            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
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
                        val textX = (itemView.right - textWidth - 40)
//                        val textX = dX + textPadding
                        val textY = (itemView.top + itemHeight / 2 + textPaint.textSize / 2 - 8).toFloat()
                        c.drawText(text, textX, textY, textPaint)

                    }
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        // 체크 리스트 삭제
        val itemCallback2 = object : ItemTouchHelper.SimpleCallback (
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
                // 해당 위치의 데이터 삭제
                if(auth.currentUser != null) {
                    cAdapter.removeData(viewHolder.layoutPosition)
                    cAdapter.delete(viewHolder.layoutPosition)
                }
            }

            // 꾹 눌러 이동할 수 없도록 함
            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
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
                        val textX = (itemView.right - textWidth - 40)
//                        val textX = dX + textPadding
                        val textY = (itemView.top + itemHeight / 2 + textPaint.textSize / 2 - 8).toFloat()
                        c.drawText(text, textX, textY, textPaint)

                    }
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        // 리사이클러뷰에 ItemTouchHelper 적용
        ItemTouchHelper(itemCallback).attachToRecyclerView(recyclerView)
        ItemTouchHelper(itemCallback2).attachToRecyclerView(cRecyclerView)

        return view
    }

    // DB에서 추가한 알람 불러오는 함수
    private fun setDB() {
        val currentUser = auth.currentUser
        if (currentUser != null) {  // 로그인 되어있는 경우

            val docRef = db.collection("users").document(currentUser.email!!)
                .collection("schedule")
                .whereEqualTo("sdate", selectedDate)// 선택된 날짜만 가져오도록
//                .whereGreaterThan("stime", nowText)

            docRef.get()
                .addOnSuccessListener { result ->
                    val scheduleList = mutableListOf<ScheduleModel>()
                    val idList = mutableListOf<String>()
                    for (document in result) {
//                        Log.d("mytag", "${document.id}")
                        val schedule = document.toObject<ScheduleModel>()
                        if(schedule.sDate>todayText || (schedule.sTime>nowText && schedule.sDate==todayText)) {
                            scheduleList.add(schedule)
                            idList.add(document.id)
                        }
//                        Log.d("mytag", "${document.id} => ${document.data}")
                    }

                    rvAdapter = MonthAlarmAdapter(scheduleList, idList)

                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = rvAdapter
                    recyclerView.setHasFixedSize(true)
                }
        } else {
            Log.d("mytag", "Current user is null")
        }

    }

    private fun setListDB() { // 체크리스트 DB 가져와서 저장
        val currentUser = auth.currentUser
        if (currentUser != null) {  // 로그인 되어있는 경우

            var docRef = db.collection("users").document(currentUser.email!!)
                .collection("schedule")
                .whereEqualTo("sdate", selectedDate)
//                .whereLessThanOrEqualTo("stime", nowText)

            docRef.get()
                .addOnSuccessListener { result ->
                    val checkList = mutableListOf<TodoModel>()
                    val idList = mutableListOf<String>()
                    for (document in result) {
//                        Log.d("mytag", "${document.id}")
                        val schedule = document.toObject<ScheduleModel>()
                        if(schedule.sDate<todayText || (schedule.sTime<=nowText && schedule.sDate==todayText)) {
                            val check = TodoModel(false, schedule.aName)
                            checkList.add(check)
                            idList.add(document.id)
//                            Log.d("mytag", "${document.id} => ${document.data}")
                        }
                    }

                    cAdapter = MonthCheckAdapter(checkList, idList)

                    cRecyclerView.layoutManager = LinearLayoutManager(context)
                    cRecyclerView.adapter = cAdapter
                    cRecyclerView.setHasFixedSize(true)
                }
        }
    }

    fun dotDecorator() {

        val docRef = db.collection("users").document(auth.currentUser!!.email!!)
            .collection("schedule")
        CoroutineScope(Dispatchers.IO).launch {
            docRef.get()
                .addOnSuccessListener { result ->
                    val scheduleList = mutableListOf<ScheduleModel>()
                    for (document in result) {
                        val schedule = document.toObject<ScheduleModel>()
                        val date = schedule.sDate.split("-")
                        var cal = CalendarDay.from(date[0].toInt(), date[1].toInt()-1, date[2].toInt())
                        Log.d("mytag", "$cal")
                        binding.materialCalendar
                            .addDecorator(
                                EventDecorator(
                                    Color.parseColor("#A4A4A4"),
                                    Collections.singleton(cal)))
                        }
                    }
            }
        }

    inner class WeekdayDecorator : DayViewDecorator {

        private val calendar = Calendar.getInstance()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day!!.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return (weekDay == Calendar.SATURDAY) || (weekDay == Calendar.SUNDAY)
        }

        override fun decorate(view: DayViewFacade?) {
            view!!.addSpan(ForegroundColorSpan(Color.parseColor("#CF6262")))
        }

    }

    inner class EventDecorator() : DayViewDecorator {

        private var color = 0
        private lateinit var dates : HashSet<CalendarDay>

        constructor(color: Int, dates: Collection<CalendarDay>) : this() {
            this.color=color
            this.dates=HashSet(dates)
        }

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(7F, color))
        }
    }
}


