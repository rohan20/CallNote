package com.rohan.callnote.fragments;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rohan.callnote.BaseCallNoteFragment;
import com.rohan.callnote.R;
import com.rohan.callnote.adapters.NotesCursorAdapter;
import com.rohan.callnote.models.Note;
import com.rohan.callnote.network.APIClient;
import com.rohan.callnote.network.APIResponse;
import com.rohan.callnote.utils.Constants;
import com.rohan.callnote.utils.Contract.NotesEntry;
import com.rohan.callnote.utils.DBUtils;
import com.rohan.callnote.utils.SharedPrefsUtil;
import com.rohan.callnote.widget.CallNoteWidget;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends BaseCallNoteFragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.add_call_fab)
    FloatingActionButton mAddCallFAB;
    @BindView(R.id.notes_recycler_view)
    RecyclerView mNotesRecyclerView;

    @BindView(R.id.notes_empty_text_view)
    TextView mEmptyTextView;
    @BindView(R.id.notes_progress_bar)
    ProgressBar mProgressBar;

    private NotesCursorAdapter mAdapterNotes;
    private Cursor mCursor;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getBaseCallNoteActivity().getSupportActionBar().show();
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        ButterKnife.bind(this, v);

        getBaseCallNoteActivity().getSupportActionBar().show();
        mAddCallFAB.setOnClickListener(this);

        Toast.makeText(getBaseCallNoteActivity(), "Logged in as " + SharedPrefsUtil.retrieveStringValue(SharedPrefsUtil.USER_EMAIL, null), Toast.LENGTH_SHORT).show();

        mAdapterNotes = new NotesCursorAdapter(getBaseCallNoteActivity(), null);
        mNotesRecyclerView.setAdapter(mAdapterNotes);
        mNotesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mProgressBar.setVisibility(View.VISIBLE);
        mNotesRecyclerView.setVisibility(View.GONE);

        fetchNotesFromAPI();

        getLoaderManager().initLoader(Constants.NOTES_CURSOR_LOADER_ID, null, this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mCursor.moveToPosition(viewHolder.getAdapterPosition());
                int serverIDToBeDeleted = mCursor.getInt(mCursor.getColumnIndex(NotesEntry
                        .COLUMN_SERVER_ID));

                deleteNoteFromAPI(serverIDToBeDeleted);

                getBaseCallNoteActivity().updateWidget();
            }
        }).attachToRecyclerView(mNotesRecyclerView);

        return v;
    }

    private void deleteNoteFromAPI(final int id) {
        if (!getBaseCallNoteActivity().isNetworkConnected()) {
            Toast.makeText(getBaseCallNoteActivity(), "Please connect to the internet and try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<APIResponse> call = APIClient.getApiService().deleteNote(String.valueOf(id));

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    String selection = NotesEntry.COLUMN_SERVER_ID + " =? ";
                    String[] selectionArgs = {String.valueOf(id)};

                    getBaseCallNoteActivity().getContentResolver().delete(NotesEntry.CONTENT_URI,
                            selection, selectionArgs);

                } else {
                    Toast.makeText(getBaseCallNoteActivity(), "Unable to delete note right " +
                            "now. Please try later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getBaseCallNoteActivity(), "Unable to delete note right " +
                        "now. Please try later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchNotesFromAPI() {

        if (!getBaseCallNoteActivity().isNetworkConnected()) {
            Toast.makeText(getBaseCallNoteActivity(), "Please connect to the internet and try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<APIResponse<List<Note>>> call = APIClient.getApiService().getNotes();

        call.enqueue(new Callback<APIResponse<List<Note>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Note>>> call, Response<APIResponse<List<Note>>> response) {
                if (response.isSuccessful()) {

                    List<Note> notesList = response.body().getData();
                    Collections.reverse(notesList);

                    Vector<ContentValues> cVVector = new Vector<ContentValues>(notesList.size());

                    for (Note note : notesList) {
                        ContentValues noteValues = DBUtils.cvFromNotes(note);
                        cVVector.add(noteValues);
                    }

                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);

                        getBaseCallNoteActivity().getApplicationContext().getContentResolver().bulkInsert(
                                NotesEntry.CONTENT_URI, cvArray);
                    }

                    getBaseCallNoteActivity().updateWidget();

                    if (getLoaderManager().hasRunningLoaders())
                        getLoaderManager().restartLoader(Constants.NOTES_CURSOR_LOADER_ID, null,
                                NotesFragment.this);
                    else
                        getLoaderManager().initLoader(Constants.NOTES_CURSOR_LOADER_ID, null,
                                NotesFragment.this);


                } else {
                    Toast.makeText(getBaseCallNoteActivity(), "Unable to fetch latest notes right now. Please try later.", Toast.LENGTH_SHORT).show();
                    getBaseCallNoteActivity().dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<Note>>> call, Throwable t) {
                Toast.makeText(getBaseCallNoteActivity(), "Unable to fetch latest notes right now. Please try later.", Toast.LENGTH_SHORT).show();
                getBaseCallNoteActivity().dismissProgressDialog();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_call_fab:
                getBaseCallNoteActivity().switchFragment(new CallLogFragment(), true, CallLogFragment.class.getSimpleName());
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(Constants.NOTES_CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(), NotesEntry.CONTENT_URI,
                new String[]{NotesEntry._ID, NotesEntry.COLUMN_SERVER_ID,
                        NotesEntry.COLUMN_NUMBER, NotesEntry.COLUMN_NOTE_TEXT,
                        NotesEntry.COLUMN_CALL_TYPE, NotesEntry.COLUMN_TIMESTAMP},
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapterNotes.swapCursor(data);
        mCursor = data;

        mProgressBar.setVisibility(View.GONE);

        if (data.getCount() == 0) {
            mNotesRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mEmptyTextView.setVisibility(View.GONE);
            mNotesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterNotes.swapCursor(null);
    }

}
