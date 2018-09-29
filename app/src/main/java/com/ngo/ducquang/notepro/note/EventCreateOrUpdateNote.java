package com.ngo.ducquang.notepro.note;

/**
 * Created by ducqu on 7/28/2018.
 */

public class EventCreateOrUpdateNote {
    private NoteModel noteModel;
    private boolean isAdd = true;

    public EventCreateOrUpdateNote(NoteModel noteModel, boolean isAdd) {
        this.noteModel = noteModel;
        this.isAdd = isAdd;
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }

    public void setNoteModel(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
