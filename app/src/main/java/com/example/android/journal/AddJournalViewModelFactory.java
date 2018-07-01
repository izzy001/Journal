package com.example.android.journal;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddJournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final JournalDatabase mDb;
    private final int mJournalId;

    public AddJournalViewModelFactory(JournalDatabase database, int journalId) {
        mDb = database;
        mJournalId = journalId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T>modelClass) {
        //noinspection unchecked
        return (T) new AddJournalViewModel(mDb, mJournalId);
    }
}
