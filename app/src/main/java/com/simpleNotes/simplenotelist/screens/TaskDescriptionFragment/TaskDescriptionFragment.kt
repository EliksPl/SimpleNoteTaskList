package com.simpleNotes.simplenotelist.screens.TaskDescriptionFragment
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.get
import com.simpleNotes.simplenotelist.BUNDLE_KEY_TASK_TO_DESC
import com.simpleNotes.simplenotelist.R
import com.simpleNotes.simplenotelist.databinding.FragmentTaskDescriptionBinding
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import com.simpleNotes.simplenotelist.screens.DateTimePickerDialog.DateTimePickerDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TaskDescriptionFragment : BottomSheetDialogFragment(), DateTimePickerDialogFragment.DataTransferListener {

    private lateinit var taskData: TaskModel
    private lateinit var newTask : TaskModel
    private lateinit var viewModel: TaskDescriptionViewModel
    private lateinit var binding: FragmentTaskDescriptionBinding
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDescriptionBinding.inflate(inflater, container, false)
        taskData = arguments?.customGetSerializable(BUNDLE_KEY_TASK_TO_DESC)!!
        newTask = TaskModel(
            title = taskData.title,
            priority = taskData.priority,
            remindOnDate = taskData.remindOnDate)
        initData(taskData)
        return binding.root
    }

    private fun initData(task:TaskModel) {
        spinnerAdapter = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.task_priorities))
        binding.etTaskDescTitle.text = Editable.Factory.getInstance().newEditable(task.title)
        if (task.remindOnDate>0) {
            val data = Instant.ofEpochMilli(task.remindOnDate).atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            binding.btnTaskDescSetReminder.text = data.format(
                DateTimeFormatter.ofLocalizedDateTime(
                    FormatStyle.SHORT
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get()
//        binding.spinnerTaskDescPriorities.adapter = spinnerAdapter
//        binding.spinnerTaskDescPriorities.setSelection(taskData.priority)
        binding.etTaskDescTitle.doAfterTextChanged {
            if (it.toString().isEmpty()){
                binding.taskDescDoneBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.inactive_button_text))
                binding.taskDescDoneBtn.typeface = Typeface.DEFAULT
            }else{
                binding.taskDescDoneBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.active_button_text))
                binding.taskDescDoneBtn.typeface = Typeface.DEFAULT_BOLD
            }
        }


        binding.taskDescDoneBtn.setOnClickListener{

            newTask.title = binding.etTaskDescTitle.text.toString()
//            newTask.priority = binding.spinnerTaskDescPriorities.selectedItemPosition//binding.etDescFragmentTaskDesc.text.toString()
            newTask.priority=0

            if (taskData.title == newTask.title &&
                taskData.remindOnDate == newTask.remindOnDate &&
                taskData.priority == newTask.priority){
                dismiss()
            }
            viewModel.replaceTask(taskData,newTask)
            dismiss()
//            MAIN.navController.navigate(R.id.action_taskDescriptionFragment_to_tasksFragment)
        }

        binding.taskDescBackBtn.setOnClickListener{
//            viewModel.tryDeleteTask(taskData)
//            MAIN.navController.navigate(R.id.action_taskDescriptionFragment_to_tasksFragment)
            dismiss()
        }


        if (Build.VERSION.SDK_INT >= 33) {
            val permissionState = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                binding.btnTaskDescSetReminder.setOnClickListener {
                    Toast.makeText(requireContext(),"Notification permission denied", Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.btnTaskDescSetReminder.setOnClickListener{
                    val dateTimePickerDialog = DateTimePickerDialogFragment()
                    dateTimePickerDialog.setDataTransferListener(this)
                    dateTimePickerDialog.show(parentFragmentManager, "dateTimePicker")
                }
            }
        }else{
            binding.btnTaskDescSetReminder.setOnClickListener{
//                viewModel.createNotificationChannel()
                val dateTimePickerDialog = DateTimePickerDialogFragment()
                dateTimePickerDialog.setDataTransferListener(this)
                dateTimePickerDialog.show(parentFragmentManager, "dateTimePicker")
            }
        }




//        binding.taskDescFragmentBackBtn.setOnClickListener{
//            MAIN.navController.navigate(R.id.action_taskDescriptionFragment_to_tasksFragment)
//        }


    }

    @Suppress("DEPRECATION")
    inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializable(key, T::class.java)
        } else {
            getSerializable(key) as? T
        }
    }

    override fun onDataTransfer(data: LocalDateTime) {
        binding.btnTaskDescSetReminder.text = data.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
        newTask.remindOnDate = data.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }


}