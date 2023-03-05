package com.example.notiup

import android.content.ClipData.Item
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notiup.databinding.ActivityMainBinding
import com.example.notiup.databinding.FragmentAlarmBinding


class AlarmFragment : Fragment() {

    // 데이터 리스트
    private var alarm = ArrayList<Alarms>()
    // 어댑터
    private var rvAdapter = AlarmAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_alarm)

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
                // 해당 위치의 데이터 삭제
                rvAdapter.removeData(viewHolder.layoutPosition)

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

        with(recyclerView) {
            // 레이아웃매니저 설정
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            // 어댑터 설정
            adapter = rvAdapter

            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        // 리사이클러뷰에 ItemTouchHelper 적용
        ItemTouchHelper(itemCallback).attachToRecyclerView(recyclerView)

        return view
    }

}