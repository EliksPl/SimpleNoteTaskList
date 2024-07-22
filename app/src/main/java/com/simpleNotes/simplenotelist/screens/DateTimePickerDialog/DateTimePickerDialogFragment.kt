package com.simpleNotes.simplenotelist.screens.DateTimePickerDialog

import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.databinding.FragmentDateTimePickerDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimePickerDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDateTimePickerDialogBinding
    private lateinit var viewModel: DateTimePickerViewModel
//    private var arrActualDates = ArrayList<LocalDate>()
    private var selectedDate = LocalDate.now()
    private var selectedTime = LocalTime.now()
    private var selectedDateTime = LocalDateTime.now()
    private var dataTransferListener: DataTransferListener? = null

    interface DataTransferListener {
        fun onDataTransfer(data: LocalDateTime)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDateTimePickerDialogBinding.inflate(inflater,container,false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ViewCompat.setOnApplyWindowInsetsListener(requireDialog().window?.decorView!!) { _, insets ->
                val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val navigationBarHeight =
                    insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                binding.root.setPadding(0, 0, 0, imeHeight - navigationBarHeight)
                insets
            }
        } else {
            @Suppress("DEPRECATION")
            requireDialog().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().setCancelable(false)
        requireDialog().setCanceledOnTouchOutside(true)

        requireView().setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // Закриття BottomSheetDialogFragment
                dismiss()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

    }

    private fun init(){
        viewModel = ViewModelProvider(this).get()
        initDatePickerValues()
        updateSelectedTime(selectedDate,selectedTime)


        binding.timePicker.setIs24HourView(DateFormat.is24HourFormat(MAIN))

        //date picker listener
        binding.datePicker.setOnValueChangedListener { numberPicker, _, _ ->
            //selectedDate = arrActualDates.get(numberPicker.value)
            selectedDate = LocalDate.now().plusDays(numberPicker.value.toLong())

            updateSelectedTime(selectedDate, selectedTime)
        }


        //timepicker listener

        binding.timePicker.setOnTimeChangedListener {_,hour,minute ->
            selectedTime = LocalTime.of(hour,minute)

            updateSelectedTime(selectedDate,selectedTime)
        }

        //done buttons
        binding.selectTimeButton.setOnClickListener{
            dataTransferListener?.onDataTransfer(selectedDateTime)

            dismiss()
        }

        binding.cancelSelectingButton.setOnClickListener{
            dismissAllowingStateLoss()
        }


    }

    private fun updateSelectedTime(selectedDate: LocalDate, selectedTime: LocalTime) {
        val formatter : DateTimeFormatter
        val formatPattern = getDateFormatPattern()

        formatter = DateTimeFormatter.ofPattern(formatPattern)

        selectedDateTime = LocalDateTime.of(selectedDate,selectedTime).withSecond(0)
        binding.pickerTitle.text = selectedDateTime.format(formatter)
    }

    private fun getDateFormatPattern():String{
        var formatPattern = "EE, dd MMM HH:mm"

        if (selectedDate.year != LocalDate.now().year) {
            formatPattern = "EE, dd MMM yyyy, HH:mm"
        }

        if (!DateFormat.is24HourFormat(MAIN)){
            formatPattern = formatPattern.replace('H', 'h')
            formatPattern = formatPattern.plus(" a")
        }

        return formatPattern
    }

    private fun initDatePickerValues(){
        val arrDates = ArrayList<String>()
        val formatter = DateTimeFormatter.ofPattern("EE, dd MMM")
        val currentDate = LocalDate.now()

        arrDates.add("Today")
        //arrActualDates.add(currentDate)
        arrDates.add("Tomorrow")
        //arrActualDates.add(currentDate.plusDays(1))

        for (i in 2..365) {
            val date = currentDate.plusDays(i.toLong())
            //arrActualDates.add(date)
            arrDates.add(date.format(formatter))
        }

        binding.datePicker.minValue = 0
        binding.datePicker.maxValue = arrDates.size - 1
        binding.datePicker.wrapSelectorWheel = false
        binding.datePicker.isClickable = false
//        binding.datePicker.isFocusable = false
//        binding.timePicker.isVerticalScrollBarEnabled = false

        val stringArray = arrDates.toTypedArray()
        binding.datePicker.displayedValues = stringArray
    }

    fun setDataTransferListener(listener: DataTransferListener) {
        dataTransferListener = listener
    }

}