package com.simpleNotes.simplenotelist.screens.descriptionFragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.simpleNotes.simplenotelist.BUNDLE_KEY_NOTE_TO_DESC
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.R
import com.simpleNotes.simplenotelist.databinding.FragmentNoteDescriptionBinding
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import java.io.Serializable


class NoteDescriptionFragment : Fragment() {

    private lateinit var noteData : NoteModel
    private lateinit var binding: FragmentNoteDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteDescriptionBinding.inflate(inflater, container, false)
        noteData = arguments?.customGetSerializable(BUNDLE_KEY_NOTE_TO_DESC)!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val viewModel = ViewModelProvider(this).get<NoteDescriptionViewModel>()

        binding.DescNoteFragmentEtTitle.setText(noteData.title)
        binding.DescNoteFragmentEtNote.setText(noteData.desc)

        binding.backBtn.setOnClickListener{
            val newTitle = binding.DescNoteFragmentEtTitle.text.toString()
            val newDesc = binding.DescNoteFragmentEtNote.text.toString()


            if (noteData.title != newTitle || noteData.desc != newDesc){
                viewModel.replaceNote(noteData,
                    NoteModel(title = newTitle, desc = newDesc, date = viewModel.getDate())
                ){}
            }

            MAIN.navController.navigate(R.id.action_noteDescriptionFragment_to_notesFragment)
        }

        binding.deleteBtn.setOnClickListener{

            viewModel.tryDeleteNote(noteData){}
            MAIN.navController.navigate(R.id.action_noteDescriptionFragment_to_notesFragment)
        }


    }

    @Suppress("DEPRECATION")
    inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializable(key, T::class.java)
        } else {
            getSerializable(key) as? T
        }
    }
}