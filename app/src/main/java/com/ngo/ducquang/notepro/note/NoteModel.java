package com.ngo.ducquang.notepro.note;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by ducqu on 5/13/2018.
 */

@Entity(tableName = "note")
public class NoteModel
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "created")
    private long created;
    @ColumnInfo(name = "editFinal")
    private long editFinal;
    @ColumnInfo(name = "type")
    private int type = 1;
    @ColumnInfo(name = "favotite")
    private boolean favorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getEditFinal() {
        return editFinal;
    }

    public void setEditFinal(long editFinal) {
        this.editFinal = editFinal;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    //    private String id = "", name = "", content = "", created = "", editFinal = "";
//    private int type;
//    private boolean favorite = false;
}
