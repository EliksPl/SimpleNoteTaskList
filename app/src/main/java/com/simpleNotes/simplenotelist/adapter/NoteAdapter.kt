package com.simpleNotes.simplenotelist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpleNotes.simplenotelist.databinding.ItemNoteLayoutBinding
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import com.simpleNotes.simplenotelist.model.uiModel.rvNoteModel
import com.simpleNotes.simplenotelist.screens.notesFragment.NotesFragment
import kotlin.collections.ArrayList


class NoteAdapter(
    private val showMenuDelete: (Boolean) -> Unit
    ): RecyclerView.Adapter<NoteAdapter.TaskViewHolder>() {

    private var itemList = ArrayList<rvNoteModel>()
//    var isSelectionEnabled = false
    private var selectedItemList = mutableListOf<Int>()

    class TaskViewHolder(val binding: ItemNoteLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemNoteLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = itemList[position]

        holder.binding.noteTitle.text = item.note.title
        holder.binding.noteDesc.text = item.note.desc
        holder.binding.noteDate.text = item.note.date

        if (selectedItemList.contains(position)){
            holder.binding.checkboxSelectedItem.visibility = View.VISIBLE
        }else{
            holder.binding.checkboxSelectedItem.visibility = View.GONE
        }

        holder.itemView.setOnLongClickListener {
            selectItem(holder, item, position)
            true
        }

        holder.itemView.setOnClickListener {
            if (selectedItemList.isNotEmpty()){
                checkSelectionAndChange(item, position)
            }else{
                NotesFragment.noteClicked(itemList[position].note)
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
    private fun checkSelectionAndChange(item: rvNoteModel, position: Int) {
        if (item.selected){
            selectedItemList.remove(position)
            if (selectedItemList.size == 0){
                showMenuDelete(false)
            }

        }else{
            selectedItemList.add(position)
        }
        item.selected = !item.selected
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun selectItem(holder: TaskViewHolder, item: rvNoteModel, position: Int) {
        selectedItemList.add(position)
        item.selected = true
        showMenuDelete(true)
        notifyDataSetChanged()
    }

//    override fun onViewAttachedToWindow(holder: TaskViewHolder) {
//        super.onViewAttachedToWindow(holder)
//
////        holder.itemView.setOnClickListener {
////            NotesFragment.noteClicked(itemList[holder.adapterPosition].note)
////        }
//
////        holder.itemView.setOnLongClickListener {
////            itemList[holder.adapterPosition].selected = true
////        }
//    }

    override fun onViewDetachedFromWindow(holder: TaskViewHolder) {
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return this.itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<NoteModel>){
        itemList.clear()
        for (note in newList){
            this.itemList.add(rvNoteModel(note = note))
        }

        itemList.reverse()
        notifyDataSetChanged()
    }

    fun getSelectedNotesForDelete():ArrayList<NoteModel> {
        var notesList = ArrayList<NoteModel>()
        if (selectedItemList.isNotEmpty()){
            for (position in selectedItemList){
                notesList.add(itemList[position].note)
            }
            selectedItemList.clear()
        }
        return notesList
    }

}