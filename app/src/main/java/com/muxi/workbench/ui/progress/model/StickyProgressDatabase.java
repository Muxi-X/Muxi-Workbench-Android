package com.muxi.workbench.ui.progress.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities =  {StickyProgress.class}, version = 2 , exportSchema = false)
public abstract class StickyProgressDatabase extends RoomDatabase {

    private static StickyProgressDatabase INSTANCE;

    private static final Object sLock = new Object();

    public abstract StickyProgressDao ProgressDao();

    public static StickyProgressDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        StickyProgressDatabase.class, "StickyProgress.db").addMigrations(MIGRATION_1_2,MIGRATION_2_1).build();
            }
            return INSTANCE;
        }
    }

   static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };
    static final Migration MIGRATION_2_1 = new Migration(2, 1) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

}