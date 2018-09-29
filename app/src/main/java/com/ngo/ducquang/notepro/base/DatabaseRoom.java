package com.ngo.ducquang.notepro.base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ngo.ducquang.notepro.note.NoteDao;
import com.ngo.ducquang.notepro.note.NoteModel;

import static com.ngo.ducquang.notepro.base.DatabaseRoom.DATABASE_VERSION;

/**
 * Created by ducqu on 5/13/2018.
 */

@Database(entities = {NoteModel.class}, version = DATABASE_VERSION, exportSchema = false)
public abstract class DatabaseRoom extends RoomDatabase {
    private static DatabaseRoom instance;
    public abstract NoteDao noteDao();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Room-database";

    public static DatabaseRoom getAppDatabase(Context context)
    {
        if (instance == null)
        {
            synchronized (DatabaseRoom.class)
            {
                instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseRoom.class, DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }

        return instance;
    }
}
