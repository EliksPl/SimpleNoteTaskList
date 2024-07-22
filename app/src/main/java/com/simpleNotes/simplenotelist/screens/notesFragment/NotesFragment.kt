package com.simpleNotes.simplenotelist.screens.notesFragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.simpleNotes.simplenotelist.BUNDLE_KEY_NOTE_TO_DESC
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.R
import com.simpleNotes.simplenotelist.databinding.FragmentNotesBinding
import com.simpleNotes.simplenotelist.adapter.NoteAdapter
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import kotlinx.coroutines.launch

class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private lateinit var notesAdapter : NoteAdapter
    private lateinit var rvNoteList : RecyclerView
    lateinit var viewModel : NotesViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initToolbar()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        viewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        viewModel.initDatabase()
        rvNoteList = binding.recyclerNoteList
        notesAdapter = NoteAdapter{show -> showDeleteButton(show)}
        rvNoteList.adapter = notesAdapter



        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.allNotes().collect { notes ->
                notesAdapter.setList(notes)
            }
        }

        binding.btnAddNote.setOnClickListener {
            MAIN.navController.navigate(R.id.action_notesFragment_to_addNoteFragment)
        }

        requestNotificationPermission()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            val permissionState = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)

            // If the permission is not granted, request it.
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }

    private fun showDeleteButton(show:(Boolean)) {
        binding.notesFragmentToolBar.menu.findItem(R.id.note_menu_item_delete_selected).isVisible = show
    }

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

    private fun initToolbar() {
//        MAIN.setActionBarTitle(R.string.notesFragment_title)
//        binding.notesFragmentToolBar.inflateMenu(R.menu.popup_menu_notes)

        showDeleteButton(false)
        binding.notesFragmentToolBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.popup_menu_item_settings -> {
                    MAIN.navController.navigate(R.id.nav_bar_item_settings)
                    return@setOnMenuItemClickListener true
                }
                R.id.popup_menu_item_contactUs -> {
                    return@setOnMenuItemClickListener true
                }
                R.id.note_menu_item_delete_selected ->{
                    delete()
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
//        when(menuItem.itemId){
//            R.id.popup_menu_item_settings -> {
//                MAIN.navController.navigate(R.id.nav_bar_item_settings)
//                return true
//            }
//            R.id.popup_menu_item_contactUs -> {
//                return true
//            }
//        }
//        return false

    }

    private fun delete() {
        val alertDialog = AlertDialog.Builder(MAIN)
        alertDialog.setTitle(R.string.delete_selected_items_request_title)
        alertDialog.setMessage(R.string.delete_selected_items_request)
        alertDialog.setPositiveButton(R.string.default_positive_button_text){_,_->
            val notesToDelete = notesAdapter.getSelectedNotesForDelete()

            viewModel.deleteSelectedNotes(notesToDelete)
            showDeleteButton(false)
        }
        alertDialog.setNegativeButton(R.string.default_negative_button_text){_,_->}
        alertDialog.show()
    }

    companion object{
        fun noteClicked(noteModel: NoteModel){
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_KEY_NOTE_TO_DESC, noteModel)
            MAIN.navController.navigate(R.id.action_notesFragment_to_noteDescriptionFragment, bundle)
        }
    }

}