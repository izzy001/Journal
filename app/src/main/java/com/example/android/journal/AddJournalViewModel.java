package com.example.android.journal;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class AddJournalViewModel extends ViewModel {

    //task member variable for the JournalEntry object wrapped in a LiveData
    private LiveData<JournalEntry> journal;

    // A constructor where you call loadJournalById of the taskDao to initialize the tasks variable
     // Note: The constructor should receive the database and the taskId

    public AddJournalViewModel(JournalDatabase database, int journalId) {
        journal = database.journalDao().loadJournalById(journalId);
    }

    public LiveData<JournalEntry> getJournal() { return journal; }
}
