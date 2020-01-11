package com.muxi.workbench.ui.progress.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "StickyProgress")
public final class StickyProgress {

    @PrimaryKey
    @ColumnInfo(name = "sid")
    private final int sid;

    public StickyProgress(@NonNull int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }
}
