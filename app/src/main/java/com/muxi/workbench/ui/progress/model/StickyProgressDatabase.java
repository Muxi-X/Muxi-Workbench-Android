package com.muxi.workbench.ui.progress.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities =  {StickyProgress.class}, version = 1, exportSchema = false)
public abstract class StickyProgressDatabase extends RoomDatabase {

    private static StickyProgressDatabase INSTANCE;

    private static final Object sLock = new Object();

    public abstract StickyProgressDao stickyProgressDao();

    public static StickyProgressDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        StickyProgressDatabase.class, "StickyProgress.db").build();
            }
            return INSTANCE;
        }
    }

}