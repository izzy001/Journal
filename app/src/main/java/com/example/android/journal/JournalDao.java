package com.example.android.journal;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal")
    LiveData<List<JournalEntry>> loadAllJournal();

   @Insert
    void insertJournal(JournalEntry journalEntry);

   @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournal(JournalEntry journalEntry);

   @Delete
    void deleteJournal(JournalEntry journalEntry);

   @Query("SELECT * FROM journal WHERE id = :id")
    LiveData<JournalEntry> loadJournalById(int id);
}
