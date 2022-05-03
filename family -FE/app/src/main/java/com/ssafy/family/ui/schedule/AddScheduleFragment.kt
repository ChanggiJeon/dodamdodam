package com.ssafy.family.ui.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.messaging.FirebaseMessaging
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.data.remote.req.ScheduleReq
import com.ssafy.family.databinding.CalendarHeaderBinding
import com.ssafy.family.databinding.CalendarSelectingDayBinding
import com.ssafy.family.databinding.FragmentAddScheduleBinding
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.util.CalendarUtil
import com.ssafy.family.util.CalendarUtil.getDrawableCompat
import com.ssafy.family.util.CalendarUtil.makeInVisible
import com.ssafy.family.util.CalendarUtil.makeVisible
import com.ssafy.family.util.CalendarUtil.setTextColorRes
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class AddScheduleFragment : Fragment() {

    private lateinit var binding: FragmentAddScheduleBinding
    private val addScheduleViewModel by activityViewModels<AddScheduleViewModel>()

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val headerDateFormatter = DateTimeFormatter.ofPattern("MMM d'일'")
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    private val startBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(R.drawable.box_calendar_start) as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(R.drawable.box_calendar_end) as GradientDrawable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as ScheduleActivity).changeHeader("일정 추가","취소", "저장")

        (activity as ScheduleActivity).binding.scheduleButtonInclude.button.setOnClickListener {
            requireActivity().finish()
        }

        (activity as ScheduleActivity).binding.scheduleButtonInclude.button2.setOnClickListener {
            if(binding.scheduleTitle.text.isNotEmpty() && binding.scheduleContent.text!!.isNotEmpty()
                && startDate != null && endDate != null){
                addScheduleViewModel.addSchedule(
                    ScheduleReq(
                        binding.scheduleTitle.text.toString(),
                        binding.scheduleContent.text.toString(),
                        startDate!!,
                        endDate!!
                    )
                )
            }else{
                Toast.makeText(requireContext(), "일정 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        (activity as ScheduleActivity).binding.scheduleTopInclude.backbtn.setOnClickListener {
            requireActivity().finish()
        }

        val daysOfWeek = CalendarUtil.daysOfWeekFromLocale()
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
            val binding = CalendarSelectingDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        val date = day.date
                        if (startDate != null) {
                            if (date < startDate || endDate != startDate) {
                                startDate = date
                                endDate = date
                            } else if (date != startDate) {
                                endDate = date
                            }
                        } else {
                            startDate = date
                            endDate = date
                        }
                        this@AddScheduleFragment.binding.calendar.notifyCalendarChanged()
                        bindSummaryViews()
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
                val roundBgView = container.binding.roundBackground

                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()

                val startDate = startDate
                val endDate = endDate

                when (day.owner) {
                    DayOwner.THIS_MONTH -> {
                        textView.text = day.day.toString()

                        when {
                            day.date == startDate && startDate != endDate -> {
                                textView.setTextColorRes(R.color.white)
                                textView.background = startBackground
                            }
                            startDate == day.date  -> {
                                textView.setTextColorRes(R.color.white)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.box_calendar_single)
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView.setTextColorRes(R.color.white)
                                textView.setBackgroundResource(R.drawable.box_calendar_middle)
                            }
                            day.date == endDate -> {
                                textView.setTextColorRes(R.color.white)
                                textView.background = endBackground
                            }
                            day.date == today -> {
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_today)
                            }
                            else -> textView.setTextColorRes(R.color.black)
                        }
                    }
                    // 선택한 날짜가 월이 바뀌었을 경우
                    DayOwner.PREVIOUS_MONTH ->
                        if (startDate != null && endDate != null && isInDateBetween(day.date, startDate, endDate)) {
                            textView.setBackgroundResource(R.drawable.box_calendar_middle)
                        }
                    DayOwner.NEXT_MONTH ->
                        if (startDate != null && endDate != null && isOutDateBetween(day.date, startDate, endDate)) {
                            textView.setBackgroundResource(R.drawable.box_calendar_middle)
                        }
                }
            }
        }

        //달력 스크롤 리스너
        binding.calendar.monthScrollListener = {
            selectDate(it.yearMonth.atDay(1))
        }

        //요일 헤더 선언
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout
        }
        binding.calendar.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {}
        }

        addScheduleViewModel.addRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "일정 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                    requireActivity().finish()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }
        }
    }

    private fun setLoading() {
        binding.progressBarAddSLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarAddSLoading.visibility = View.GONE
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendar.notifyDateChanged(it) }
            binding.calendar.notifyDateChanged(date)
//            updateAdapterForDate(date)
//            binding.selectedMonthText.setText("${date.year}년 ${date.monthValue}월 ")
            Log.d("xxxxx", "selectDate: $date")
        }
    }

    private fun bindSummaryViews() {
        binding.startdate.apply {
            if (startDate != null) {
                text = headerDateFormatter.format(startDate)
            } else {
                text = "시작"
                setTextColor(Color.GRAY)
            }
        }

        binding.enddate.apply {
            if (endDate != null) {
                text = headerDateFormatter.format(endDate)
            } else {
                text = "끝"
                setTextColor(Color.GRAY)
            }
        }

        // Enable save button if a range is selected or no date is selected at all, Airbnb style.
        //binding.exFourSaveButton.isEnabled = endDate != null || (startDate == null && endDate == null)
    }

    private fun isInDateBetween(inDate: LocalDate, startDate: LocalDate, endDate: LocalDate): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (inDate.yearMonth == startDate.yearMonth) return true
        val firstDateInThisMonth = inDate.plusMonths(1).yearMonth.atDay(1)
        return firstDateInThisMonth >= startDate && firstDateInThisMonth <= endDate && startDate != firstDateInThisMonth
    }

    private fun isOutDateBetween(outDate: LocalDate, startDate: LocalDate, endDate: LocalDate): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (outDate.yearMonth == endDate.yearMonth) return true
        val lastDateInThisMonth = outDate.minusMonths(1).yearMonth.atEndOfMonth()
        return lastDateInThisMonth >= startDate && lastDateInThisMonth <= endDate && endDate != lastDateInThisMonth
    }
}