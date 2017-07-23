package com.rohan.callnote2.fragments;


import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rohan.callnote2.BaseCallNoteFragment;
import com.rohan.callnote2.R;
import com.rohan.callnote2.adapters.NotesAdapter;
import com.rohan.callnote2.models.Note;
import com.rohan.callnote2.network.ApiClient;
import com.rohan.callnote2.network.ApiResponse;
import com.rohan.callnote2.service.DeleteNoteService;
import com.rohan.callnote2.utils.Constants;
import com.rohan.callnote2.data.NoteContract.NotesEntry;
import com.rohan.callnote2.utils.DBUtil;
import com.rohan.callnote2.utils.UserUtil;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


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

    private NotesAdapter mAdapterNotes;
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

        mProgressBar.setVisibility(View.VISIBLE);
        mNotesRecyclerView.setVisibility(View.GONE);

        setupNotesFragment();

        return v;
    }

    private void setupNotesFragment() {

        Log.e("onn", "setupNotesFragment()");

        mAdapterNotes = new NotesAdapter(getBaseCallNoteActivity(), null);
        mNotesRecyclerView.setAdapter(mAdapterNotes);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (2, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                // TODO: 12-Jul-17 Correct this; What's causing the delay? Loader, CProvider
                // or Network Call?
                getBaseCallNoteActivity().dismissFetchingNotesProgressDialog();
            }
        };

        mNotesRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        fetchNotesFromAPI();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.e("onn", "onSwiped()");

                mCursor.moveToPosition(viewHolder.getAdapterPosition());

                int noteToBeDeletedId = mCursor.getInt(mCursor.getColumnIndex(NotesEntry
                        .COLUMN_SERVER_ID));

                if (!getBaseCallNoteActivity().isNetworkConnected()) {
                    getBaseCallNoteActivity().showSnackbar(getString(R.string.please_connect_to_the_internet_error));
                    return;
                }

                Intent intent = new Intent(getBaseCallNoteActivity(), DeleteNoteService.class);
                intent.putExtra(Constants.NOTE_TO_BE_DELETED, noteToBeDeletedId);
                getBaseCallNoteActivity().startService(intent);

                // TODO: 13-Jul-17
//                getLoaderManager().restartLoader(Constants.NOTES_CURSOR_LOADER_ID, null,
//                        NotesFragment.this);
            }
        }).attachToRecyclerView(mNotesRecyclerView);

//        getLoaderManager().initLoader(Constants.NOTES_CURSOR_LOADER_ID, null, this);
        Log.e("onn", "initLoader()");
    }

    private void fetchNotesFromAPI() {

        Log.e("onn", "fetchNotesFromAPI()");

        if (!getBaseCallNoteActivity().isNetworkConnected()) {
            getBaseCallNoteActivity().showSnackbar(getString(R.string.please_connect_to_the_internet_error));
            return;
        }

        Call<ApiResponse<List<Note>>> call = ApiClient.getApiService().getNotes();

        call.enqueue(new Callback<ApiResponse<List<Note>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Note>>> call, Response<ApiResponse<List<Note>>> response) {
                if (response.isSuccessful()) {

                    Log.e("onn", "success");

                    List<Note> notesList = response.body().getData();
                    Collections.reverse(notesList);

                    Vector<ContentValues> cVVector = new Vector<>(notesList.size());

                    for (Note note : notesList) {
                        note.setEmail(UserUtil.getEmail());
                        ContentValues noteValues = DBUtil.cvFromNotes(note);
                        cVVector.add(noteValues);
                    }

                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);

                        if (getBaseCallNoteActivity() != null)
                            getBaseCallNoteActivity().getApplicationContext().getContentResolver()
                                    .bulkInsert(NotesEntry.CONTENT_URI, cvArray);
                    }

                    if (getBaseCallNoteActivity() != null)
                        getBaseCallNoteActivity().updateWidget();

                    if (isAdded()) {

                        Log.e("onn", "isAdded()");

                        if (getLoaderManager().hasRunningLoaders()) {
                            Log.e("onn", "hasRunningLoaders()");
                            getLoaderManager().restartLoader(Constants.NOTES_CURSOR_LOADER_ID, null,
                                    NotesFragment.this);
                        } else {
                            Log.e("onn", "does not have running Loaders()");
                            getLoaderManager().initLoader(Constants.NOTES_CURSOR_LOADER_ID, null,
                                    NotesFragment.this);
                        }
                    }

                } else {
                    getBaseCallNoteActivity().showSnackbar(getString(R.string.unable_to_fetch_notes));
                    getBaseCallNoteActivity().dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Note>>> call, Throwable t) {
                getBaseCallNoteActivity().showSnackbar(getString(R.string.unable_to_fetch_notes));
                getBaseCallNoteActivity().dismissProgressDialog();
            }
        });

    }

    private void fabClicked() {
        if (ContextCompat.checkSelfPermission(getBaseCallNoteActivity(),
                android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getBaseCallNoteActivity(),
                    new String[]{android.Manifest.permission.READ_CALL_LOG},
                    Constants.MY_PERMISSIONS_REQUEST_READ_CALL_LOG_FAB);

        } else {
            getBaseCallNoteActivity().switchFragment(new CallLogFragment(), true, CallLogFragment.class.getSimpleName());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_call_fab:
                fabClicked();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constants.MY_PERMISSIONS_REQUEST_READ_CALL_LOG_FAB: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getBaseCallNoteActivity().switchFragment(new CallLogFragment(), true, CallLogFragment.class.getSimpleName());
                    break;

                } else {
                    getBaseCallNoteActivity().showSnackbar(getString(R.string.permission_required_call_log));
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            case Constants.MY_PERMISSIONS_REQUEST_READ_CALL_LOG: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (mAdapterNotes != null)
                        mAdapterNotes.notifyDataSetChanged();
                    else
                        mAdapterNotes = new NotesAdapter(getBaseCallNoteActivity(), null);

                    break;

                } else {
                    getBaseCallNoteActivity().showSnackbar(getString(R.string.permission_required_call_log));
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        getLoaderManager().restartLoader(Constants.NOTES_CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.e("onn", "onCreateLoader()");

        String[] projection = new String[]{
                NotesEntry._ID, NotesEntry.COLUMN_SERVER_ID,
                NotesEntry.COLUMN_NUMBER, NotesEntry.COLUMN_NOTE_TEXT,
                NotesEntry.COLUMN_CALL_TYPE, NotesEntry.COLUMN_TIMESTAMP,
                NotesEntry.COLUMN_CURRENT_USER_EMAIL
        };
        String selection = NotesEntry.COLUMN_CURRENT_USER_EMAIL + "=?";
        String[] selectionArgs = new String[]{
                UserUtil.getEmail()
        };

        return new CursorLoader(getActivity(),
                NotesEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (mAdapterNotes != null)
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

        getBaseCallNoteActivity().updateWidget();

        Log.e("onn", "onLoadFinished()");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapterNotes != null) {
            mAdapterNotes.swapCursor(null);
            Log.e("onn", "onLoaderReset() != null");
        }

        Log.e("onn", "onLoaderReset() = null");
    }

}
