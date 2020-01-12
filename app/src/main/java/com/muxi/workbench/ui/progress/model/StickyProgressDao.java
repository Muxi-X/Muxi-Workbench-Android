package com.muxi.workbench.ui.progress.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface StickyProgressDao {

    @Query("SELECT * FROM StickyProgress")
    List<Integer> getStickyProgressList();

    @Query( "DELETE FROM StickyProgress WHERE sid =:sid")
     void deleteStickyProgress(int sid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addStickyProgress(StickyProgress stickyProgress);

}
