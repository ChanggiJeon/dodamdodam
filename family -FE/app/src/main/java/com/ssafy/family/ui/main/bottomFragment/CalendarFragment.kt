package com.ssafy.family.ui.main.bottomFragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.ssafy.family.R
import com.ssafy.family.data.Event
import com.ssafy.family.databinding.CalendarDayBinding
import com.ssafy.family.databinding.CalendarHeaderBinding
import com.ssafy.family.databinding.FragmentCalendarBinding
import com.ssafy.family.ui.Adapter.ScheduleAdapter
import com.ssafy.family.util.CalendarUtil
import com.ssafy.family.util.CalendarUtil.daysOfWeekFromLocale
import com.ssafy.family.util.CalendarUtil.inputMethodManager
import com.ssafy.family.util.CalendarUtil.makeInVisible
import com.ssafy.family.util.CalendarUtil.makeVisible
import com.ssafy.family.util.CalendarUtil.setTextColorRes
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    private var mContext: Context? = null
    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    lateinit var eventsAdapter:ScheduleAdapter
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val selectionFormatter = DateTimeFormatter.ofPattern("MMM d일")
    private val events = mutableMapOf<LocalDate, List<Event>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventsAdapter = ScheduleAdapter(mContext!!) {
            AlertDialog.Builder(mContext!!)
                .setMessage("일정 삭제")
                .setPositiveButton("삭제") { _, _ ->
                    deleteEvent(it)
                }
                .setNegativeButton("닫기", null)
                .show()
        }

        binding.recyclerSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
        }

        val daysOfWeek = daysOfWeekFromLocale()
        var currentMonth = YearMonth.now()
        binding.calendar.apply {
            setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            binding.calendar.post {
                // Show today's events initially.
                selectDate(today)
            }
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date)
                    }
                }
            }
        }

        //일자 설정(날짜, 일정, 오늘 날짜 등)
        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.dayText
                val dotView = container.binding.dotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when (day.date) {
                        today -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.circle_schedule)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.circle_selsected_schedule)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                            dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                        }
                    }
                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }

        //달력 스크롤 리스너
        binding.calendar.monthScrollListener = {
            selectDate(it.yearMonth.atDay(1))
        }

        //달력 좌우 탐색 버튼
        binding.claendarAfterButton.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            binding.calendar.scrollToMonth(currentMonth)
        }
        binding.claendarBeforeButton.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            binding.calendar.scrollToMonth(currentMonth)
        }

        //일정 추가 버튼
        binding.addButton.setOnClickListener {
            inputDialog.show()
        }

        //요일 헤더 선언
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout
        }

        binding.calendar.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
//                if (container.legendLayout.tag == null) {
//                    container.legendLayout.tag = month.yearMonth
//                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
//                        tv.text = daysOfWeek[index].name.first().toString()
//                        tv.setTextColorRes(R.color.black)
//                    }
//                }
            }
        }
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendar.notifyDateChanged(it) }
            binding.calendar.notifyDateChanged(date)
            updateAdapterForDate(date)
            binding.selectedMonthText.setText("${date.year}년 ${date.monthValue}월 ")
        }
    }

    private fun saveEvent(text: String) {
        if (text.isBlank()) {
            Toast.makeText(requireContext(), "일정 등록 완료", Toast.LENGTH_LONG).show()
        } else {
            selectedDate?.let {
                events[it] = events[it].orEmpty().plus(Event(UUID.randomUUID().toString(), text, it))
                updateAdapterForDate(it)
            }
        }
    }

    private fun deleteEvent(event: Event) {
        val date = event.date
        events[date] = events[date].orEmpty().minus(event)
        updateAdapterForDate(date)
    }

    private fun updateAdapterForDate(date: LocalDate) {
        eventsAdapter.apply {
            events.clear()
            events.addAll(this@CalendarFragment.events[date].orEmpty())
            notifyDataSetChanged()
        }
        binding.selectedDateText.text = selectionFormatter.format(date)
    }

    private val inputDialog by lazy {
        val editText = AppCompatEditText(requireContext())
        val layout = FrameLayout(requireContext()).apply {
            // Setting the padding on the EditText only pads the input area
            // not the entire EditText so we wrap it in a FrameLayout.
            val padding = CalendarUtil.dpToPx(20, requireContext())
            setPadding(padding, padding, padding, padding)
            addView(editText, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
        AlertDialog.Builder(requireContext())
            .setTitle("일정 등록")
            .setView(layout)
            .setPositiveButton("저장") { _, _ ->
                saveEvent(editText.text.toString())
                // Prepare EditText for reuse.
                editText.setText("")
            }
            .setNegativeButton("닫기", null)
            .create()
            .apply {
                setOnShowListener {
                    // Show the keyboard
                    editText.requestFocus()
                    context.inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                }
                setOnDismissListener {
                    // Hide the keyboard
                    context.inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
    }
}
