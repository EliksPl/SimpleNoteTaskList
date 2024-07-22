package com.simpleNotes.simplenotelist.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpleNotes.simplenotelist.databinding.ItemTaskLayoutBinding
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import java.text.DateFormat
import java.util.*


class TaskAdapter(
    private val showMenuDelete: (Boolean) -> Unit
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var itemList = ArrayList<TaskModel>()
    private var selectedItemList = mutableListOf<Int>()
    var onItemClick : ((TaskModel) -> Unit)? = null

    class TaskViewHolder(val binding: ItemTaskLayoutBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemTaskLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = itemList[position]

        holder.binding.taskTitle.text = item.title
        holder.binding.taskDesc.visibility = View.VISIBLE
        when (item.priority) {
            1 -> holder.binding.taskDesc.visibility = View.GONE
            2 -> holder.binding.taskDesc.visibility = View.GONE
            3 -> holder.binding.taskDesc.visibility = View.GONE
            else -> { // Note the block
                holder.binding.taskDesc.visibility = View.GONE
            }
        }


//        holder.binding.taskDesc.text = item.priority.toString()
        if (item.remindOnDate>0) {
            holder.binding.taskDate.text = DateFormat.getInstance().format(item.remindOnDate)
            holder.binding.taskDate.visibility = View.VISIBLE
        }else{
            holder.binding.taskDate.visibility = View.GONE
        }
        if (selectedItemList.contains(position)){
            holder.binding.checkboxSelectedItemTask.visibility = View.VISIBLE
        }else{
            holder.binding.checkboxSelectedItemTask.visibility = View.GONE
        }

        holder.itemView.setOnLongClickListener {
            selectItem(holder, item, position)
            true
        }

        holder.itemView.setOnClickListener {
            if (selectedItemList.isNotEmpty()){
                checkSelectionAndChange(item, position)
            }else{
//                TasksFragment.taskClicked(itemList[position])
                onItemClick?.invoke(item)

            }
//            if (selectedItemList.contains(position)){
//                selectedItemList.remove(position)
//
//                item.selected = false
//            }else if(isSelectionEnabled){
//                NotesFragment.noteClicked(itemList[position].note)
//            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun selectItem(holder: TaskViewHolder, item: TaskModel, position: Int) {
        selectedItemList.add(position)
//        item.selected = true
        showMenuDelete(true)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkSelectionAndChange(item: TaskModel, position: Int) {
        if (selectedItemList.contains(position)){
            selectedItemList.remove(position)
            if (selectedItemList.size == 0){
                showMenuDelete(false)
            }

        }else{
            selectedItemList.add(position)
        }
//        item.selected = !item.selected
        notifyDataSetChanged()
    }

//    override fun onViewAttachedToWindow(holder: TaskViewHolder) {
//        super.onViewAttachedToWindow(holder)
////        holder.itemView.setOnClickListener {
////            TasksFragment.taskClicked(itemList[holder.adapterPosition])
////        }
//    }

    override fun onViewDetachedFromWindow(holder: TaskViewHolder) {
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return this.itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<TaskModel>){
        itemList.clear()
        this.itemList.addAll(newList)
        notifyDataSetChanged()
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun clearList(){
//        this.itemList = ArrayList<TaskModel>()
//        notifyDataSetChanged()
//    }

//    fun getTaskList() : List<TaskModel>{
//        return this.itemList
//    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun addTask(newTask : TaskModel){
//        this.itemList.add(newTask)
//        notifyDataSetChanged()
//    }


//    interface OnItemDeletedListener {
//        fun onItemDeleted(item: TaskModel)
//    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun removeTask(position: Int):TaskModel{
//        val deletedItem = itemList[position]
////        onItemDeletedListener.onItemDeleted(deletedItem)
////        this.itemList.remove(deleteTask)
////        notifyDataSetChanged()
//        return deletedItem
//    }

    fun getSelectedTasksForDelete():ArrayList<TaskModel> {
        var tasksList = ArrayList<TaskModel>()
        if (selectedItemList.isNotEmpty()){
            for (position in selectedItemList){
                tasksList.add(itemList[position])
            }
            selectedItemList.clear()
        }
        return tasksList
    }


}