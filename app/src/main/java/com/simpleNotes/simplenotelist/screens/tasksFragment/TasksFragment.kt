package com.simpleNotes.simplenotelist.screens.tasksFragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.simpleNotes.simplenotelist.BUNDLE_KEY_TASK_TO_DESC
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.R
import com.simpleNotes.simplenotelist.adapter.TaskAdapter
import com.simpleNotes.simplenotelist.databinding.FragmentTasksBinding
import com.simpleNotes.simplenotelist.permissions.ProtectedAppsController
import com.simpleNotes.simplenotelist.screens.TaskDescriptionFragment.TaskDescriptionFragment
import com.simpleNotes.simplenotelist.screens.addTaskFragment.AddTaskFragment
import kotlinx.coroutines.launch


class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private lateinit var tasksAdapter : TaskAdapter
    private lateinit var rvTaskList : RecyclerView
    private lateinit var viewModel:TasksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater)
        initToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    //double click back button to exit here
    override fun onAttach(context: Context) {
        super.onAttach(context)
        var doubleBackToExitPressedOnce = false
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    MAIN.finish()
                }else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(requireContext(), "click back button again", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({doubleBackToExitPressedOnce = false}, 2000)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        rvTaskList = binding.recyclerTaskList
        tasksAdapter = TaskAdapter{show -> showDeleteButton(show)}

        rvTaskList.adapter = tasksAdapter

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.allTasks().collect { tasks ->
                tasksAdapter.setList(tasks)
            }
        }



        tasksAdapter.onItemClick={
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_KEY_TASK_TO_DESC, it)
            val taskDescFragment = TaskDescriptionFragment()
            taskDescFragment.arguments=bundle
            taskDescFragment.show(parentFragmentManager, "descTask")
        }

        binding.btnAddTask.setOnClickListener{
//            MAIN.navController.navigate(R.id.action_tasksFragment_to_addTaskFragment)
            val addTaskFragment = AddTaskFragment()
            addTaskFragment.show(parentFragmentManager, "addTask")
        }

        ProtectedAppsController().startPowerSaverIntentIfFirsTime(MAIN)

    }



    private fun showDeleteButton(show: Boolean) {
        binding.tasksFragmentToolBar.menu.findItem(R.id.task_menu_item_delete_selected).isVisible = show
    }

    private fun initToolbar() {
        showDeleteButton(false)
        binding.tasksFragmentToolBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.popup_menu_item_settings -> {
                    MAIN.navController.navigate(R.id.nav_bar_item_settings)
                    return@setOnMenuItemClickListener true
                }
                R.id.popup_menu_item_contactUs -> {
                    return@setOnMenuItemClickListener true
                }
                R.id.task_menu_item_delete_selected ->{
                    delete()
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun delete() {
        val alertDialog = AlertDialog.Builder(MAIN)
        alertDialog.setTitle(R.string.delete_selected_items_request_title)
        alertDialog.setMessage(R.string.delete_selected_items_request)
        alertDialog.setPositiveButton(R.string.default_positive_button_text){_,_->
            val notesToDelete = tasksAdapter.getSelectedTasksForDelete()

            viewModel.deleteSelectedTasks(notesToDelete)
            showDeleteButton(false)
        }
        alertDialog.setNegativeButton(R.string.default_negative_button_text){_,_->}
        alertDialog.show()
    }

//    companion object{
//        fun taskClicked(taskModel: TaskModel){
//            val bundle = Bundle()
//            bundle.putSerializable(BUNDLE_KEY_TASK_TO_DESC, taskModel)
////            MAIN.navController.navigate(R.id.action_tasksFragment_to_taskDescriptionFragment, bundle)
//        }
//    }

//    override fun onItemDeleted(item: TaskModel) {
//        viewModel.deleteNote(item)
//    }


}