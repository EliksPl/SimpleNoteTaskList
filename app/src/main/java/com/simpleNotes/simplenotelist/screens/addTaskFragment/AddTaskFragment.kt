package com.simpleNotes.simplenotelist.screens.addTaskFragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.get
import com.simpleNotes.simplenotelist.*
import com.simpleNotes.simplenotelist.databinding.FragmentAddTaskBinding
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import com.simpleNotes.simplenotelist.screens.DateTimePickerDialog.DateTimePickerDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddTaskFragment : BottomSheetDialogFragment(), DateTimePickerDialogFragment.DataTransferListener {
    private lateinit var viewModel: AddTaskViewModel
    private lateinit var binding: FragmentAddTaskBinding
    private var selectedDateTimeTimestamp : Long = -1 //змінна для збереження даних з DateTimePicker для нагадуання.
    private lateinit var spinnerAdapter: ArrayAdapter<String>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container,false)
        spinnerAdapter = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_dropdown_item,resources.getStringArray(R.array.task_priorities))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(true)
        requireView().setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // Закриття BottomSheetDialogFragment
                dismiss()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

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
    }

    //ініціалізація слухачів кнопок та ViewModel
    private fun init() {
        viewModel = ViewModelProvider(this).get()
//        binding.spinnerAddTaskPriorities.adapter = spinnerAdapter



        //кнопка повернення, відкидання змін
        binding.addTaskBackBtn.setOnClickListener{
            dismiss()
//            MAIN.navController.navigate(R.id.action_addTaskFragment_to_tasksFragment)
        }

        binding.etTaskTitle.doAfterTextChanged {
            if (it.toString().isEmpty()){
                binding.addTaskDoneBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.inactive_button_text))
                binding.addTaskDoneBtn.typeface = Typeface.DEFAULT
            }else{
                binding.addTaskDoneBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.active_button_text))
                binding.addTaskDoneBtn.typeface = Typeface.DEFAULT_BOLD
            }
        }

        binding.addTaskDoneBtn.setOnClickListener{
            if (binding.etTaskTitle.text.isEmpty()){
                return@setOnClickListener
            }
            val title = binding.etTaskTitle.text.toString()
//            val desc = binding.spinnerAddTaskPriorities.selectedItemPosition//binding.etTaskDesc.text.toString()
            val date = selectedDateTimeTimestamp

            val task = TaskModel(
                0,
                title = title,
                priority = 0,
                remindOnDate = date
            )
            viewModel.tryInsert(task)

            dismiss()
//            MAIN.navController.navigate(R.id.action_addTaskFragment_to_tasksFragment)
        }

        if (Build.VERSION.SDK_INT >= 33) {
            val permissionState = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                binding.btnTaskSetReminder.setOnClickListener {
                    Toast.makeText(requireContext(),"Notification permission denied",Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.btnTaskSetReminder.setOnClickListener{
                    viewModel.createNotificationChannel()
                    val dateTimePickerDialog = DateTimePickerDialogFragment()
                    dateTimePickerDialog.setDataTransferListener(this)
                    dateTimePickerDialog.show(parentFragmentManager, "dateTimePicker")
                }
            }
        }else{
            binding.btnTaskSetReminder.setOnClickListener{
                viewModel.createNotificationChannel()
                val dateTimePickerDialog = DateTimePickerDialogFragment()
                dateTimePickerDialog.setDataTransferListener(this)
                dateTimePickerDialog.show(parentFragmentManager, "dateTimePicker")
            }
        }

    }

    //прийом даних(LocalDateTime) з кастомного DateTimePicker, зміна тексту кнопки, збереження у вигляді TimeStamp
    override fun onDataTransfer(data: LocalDateTime) {
        binding.btnTaskSetReminder.text = data.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
        selectedDateTimeTimestamp = data.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    }

}