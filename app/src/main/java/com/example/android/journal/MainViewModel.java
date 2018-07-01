package com.example.android.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<JournalEntry>> journal;

    public MainViewModel(@NonNull Application application) {
        super(application);
        JournalDatabase database = JournalDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the journals from the DataBase");
        journal = database.journalDao().loadAllJournal();
    }

    public LiveData<List<JournalEntry>> getJournal() { return journal; }
}
