package com.example.notiup

import android.content.Context
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
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.FragmentMonthBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashSet

class MonthFragment : Fragment() {

    private lateinit var mainActivity : MainActivity    // Activity 담긴 객체

    private lateinit var binding : FragmentMonthBinding // binding 용

    private lateinit var selectedDate: String // 달력에서 선택한 날짜

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_month, container, false)

        // 현재 날짜
        selectedDate = LocalDate.now().toString()

        binding = FragmentMonthBinding.bind(view)

        binding.materialCalendar.apply {
            setWeekDayLabels(arrayOf("일", "월", "화", "수", "목", "금", "토"))    // 요일을 한글로 설정
            setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
            addDecorator(WeekdayDecorator())
            setHeaderTextAppearance(R.style.CalendarWidgetHeader)
            setTopbarVisible(false)     // Topbar안보이게
        }

        // 달력 날짜 선택 Listener
        binding.materialCalendar.setOnDateChangedListener { _, date, _ ->
            var year = date.year
            var month = date.month + 1
            var day = date.day
            selectedDate = "$year-$month-$day"
            Log.d("mytag", selectedDate)
        }

        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .build()

        binding.materialCalendar.selectedDate = CalendarDay.today()

        //recyclerView 내용 (알람)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_container)

        val dataList = mutableListOf<String>()
        for(i in 1 .. 3) dataList.add(i.toString())

        val rvAdapter = MonthAlarmAdapter(dataList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = rvAdapter
        recyclerView.setHasFixedSize(true)

        //recyclerView 내용 (체크리스트)
        val cRecyclerView = view.findViewById<RecyclerView>(R.id.checklist_container)

        val cDataList = mutableListOf<String>()
        for(i in 1 .. 3) cDataList.add(i.toString())

        val Adapter = MonthCheckAdapter(cDataList)

        cRecyclerView.layoutManager = LinearLayoutManager(context)
        cRecyclerView.adapter = Adapter
        cRecyclerView.setHasFixedSize(true)



//        // bottom sheet 내용
//        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
//        val tagBottomSheetView = layoutInflater.inflate(R.layout.tag_bottom_sheet, null)
//
//        val bottomSheetDialog = BottomSheetDialog(mainActivity, R.style.BottomSheetDialogTheme)
//        val tagBottomSheetDialog = BottomSheetDialog(mainActivity, R.style.BottomSheetDialogTheme)
//
//        bottomSheetDialog.setContentView(bottomSheetView)
//        binding.fabEdit.setOnClickListener {
//            bottomSheetDialog.show()
//            bottomSheetDialog.behavior.state = STATE_EXPANDED
//        }
//        tagBottomSheetDialog.setContentView(tagBottomSheetView)


        binding.fabEdit.setOnClickListener {
            val bottomSheet = BottomSheet(mainActivity)
            bottomSheet.show(mainActivity.getSupportFragmentMana(), bottomSheet.tag)
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
                rvAdapter.removeData(viewHolder.layoutPosition)

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

        return view
    }

    // 선택된 날짜에 해당하는 일정 목록 가져오기
    private fun callList() {

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
}