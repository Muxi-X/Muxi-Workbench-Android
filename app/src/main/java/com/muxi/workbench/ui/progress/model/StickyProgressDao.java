package com.muxi.workbench.ui.progress.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface StickyProgressDao {

    @Query("SELECT * FROM stickyprogresses")
    List<StickyProgress> getStickyProgressList();

    @Query( "DELETE FROM stickyprogresses WHERE Sid = :sid")
     void deleteStickyProgress(int sid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addStickyProgress(StickyProgress stickyProgress);

}
