package com.simpleNotes.simplenotelist.model.uiModel

import com.simpleNotes.simplenotelist.model.dbModel.NoteModel

data class rvNoteModel(
    val note :NoteModel,
    var selected : Boolean = false
)
