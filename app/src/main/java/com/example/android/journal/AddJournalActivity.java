package com.example.android.journal;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_JOURNAL_ID = "instanceJournalId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_JOURNAL_ID = -1;

    // Constant for logging
    private static final String TAG = AddJournalActivity.class.getSimpleName();

    EditText mEditText;
    /*Button mButton;*/
    ImageButton button;

    private int mJournalId = DEFAULT_JOURNAL_ID;

    // Member variable for the Database
    private JournalDatabase mDb;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        initViews();

        mDb = JournalDatabase.getInstance(getApplicationContext());
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            /*mButton.setText("Update");*/
            if (mJournalId == DEFAULT_JOURNAL_ID) {
                //populate the UI
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);

                AddJournalViewModelFactory factory = new AddJournalViewModelFactory( mDb, mJournalId);

                final AddJournalViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddJournalViewModel.class);

                viewModel.getJournal().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                       viewModel.getJournal().removeObserver(this);
                        Log.d(TAG, "Receiving database update from LiveData");
                        populateUI(journalEntry);
                    }
                });

            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId);
        super.onSaveInstanceState(outState);
    }


    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = findViewById(R.id.editTextJournalDescription);

       ImageButton button = findViewById(R.id.saveButton);
       ImageButton button1 = findViewById(R.id.cancel_action);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddJournalActivity.this, ChildActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private void populateUI(JournalEntry task) {
        if (task == null) {
            return;
        }

        mEditText.setText(task.getDescription());

    }


    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        String description = mEditText.getText().toString();
        Date date = new Date();

        final JournalEntry task = new JournalEntry(description, date);
       JournalExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mJournalId == DEFAULT_JOURNAL_ID) {
                    // insert new task
                    mDb.journalDao().insertJournal(task);
                }else {
                    //update task
                    task.setId(mJournalId);
                    mDb.journalDao().updateJournal(task);
                }
                finish();
            }
        });
    }


}
