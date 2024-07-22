package com.simpleNotes.simplenotelist.screens.addNoteFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.R
import com.simpleNotes.simplenotelist.databinding.FragmentAddNoteBinding
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel

class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var viewModel: AddNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get()
        binding.doneBtn.setOnClickListener{
            val noteTitle = binding.AddNoteFragmentEtTitle.text.toString()
            val noteText = binding.AddNoteFragmentEtNote.text.toString()
            val noteDate = viewModel.getDate()
            viewModel.tryInsert(NoteModel(title = noteTitle, desc = noteText, date = noteDate)){}

            MAIN.navController.navigate(R.id.action_addNoteFragment_to_notesFragment)
        }

        binding.backBtn.setOnClickListener{
            MAIN.navController.navigate(R.id.action_addNoteFragment_to_notesFragment)
        }
    }





}