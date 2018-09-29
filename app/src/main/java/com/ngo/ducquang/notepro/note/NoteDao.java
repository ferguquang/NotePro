package com.ngo.ducquang.notepro.note;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by ducqu on 7/27/2018.
 */

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    List<NoteModel> getAll();

    @Insert
    void insertAll(NoteModel ... noteModels);

    @Query("DELETE FROM note")
    void deleteAll();

    @Update
    void update(NoteModel noteModel);

    @Delete
    void delete(NoteModel noteModel);

    @Insert(onConflict = REPLACE)
    void insert(NoteModel noteModel);

    @Delete
    void deleteNotes(NoteModel... items);

    @Update
    void updateNotes(NoteModel... items);

    @Query("DELETE FROM note WHERE id = (:idNote)")
    void deleteByIDNote(int idNote);

//    @Query("SELECT * FROM note WHERE id = (:idNote)")
//    void selectByID(int idNote);
}
