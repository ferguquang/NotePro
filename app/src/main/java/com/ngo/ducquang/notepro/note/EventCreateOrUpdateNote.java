package com.ngo.ducquang.notepro.note;

/**
 * Created by ducqu on 7/28/2018.
 */

public class EventCreateOrUpdateNote {
    private NoteModel noteModel;
    private boolean isUpdate = true;
    private int position = -1;


    public EventCreateOrUpdateNote(NoteModel noteModel, boolean isUpdate) {
        this.noteModel = noteModel;
        this.isUpdate = isUpdate;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }

    public void setNoteModel(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
