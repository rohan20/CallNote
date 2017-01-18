package com.rohan.callnote;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rohan.callnote.adapters.RecyclerViewAdapterCallLog;
import com.rohan.callnote.adapters.RecyclerViewAdapterNotes;
import com.rohan.callnote.models.Note;
import com.rohan.callnote.network.APIClient;
import com.rohan.callnote.network.response.ApiResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends BaseCallNoteFragment implements View.OnClickListener {

    @BindView(R.id.add_call_fab)
    FloatingActionButton mAddCallFAB;
    @BindView(R.id.notes_recycler_view)
    RecyclerView mNotesRecyclerView;

    private RecyclerViewAdapterNotes mAdapterNotes;

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

        StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapterNotes = new RecyclerViewAdapterNotes(getBaseCallNoteActivity());
        mNotesRecyclerView.setAdapter(mAdapterNotes);
        mNotesRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mAdapterNotes.setRecyclerViewNotesList(new ArrayList<Note>());

        fetchNotesFromAPI();

        return v;
    }

    private void fetchNotesFromAPI() {

        if (!getBaseCallNoteActivity().isNetworkConnected()) {
            Toast.makeText(getBaseCallNoteActivity(), "Please connect to the internet and try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        getBaseCallNoteActivity().showProgressDialog("Fetching latest notes...");

        Call<ApiResponse<List<Note>>> call = APIClient.getApiService().getNotes();

        call.enqueue(new Callback<ApiResponse<List<Note>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Note>>> call, Response<ApiResponse<List<Note>>> response) {
                if (response.isSuccessful()) {
                    // TODO: 19-Jan-17 Add all notes to db and then add notes to adapter from db

                    List<Note> notesList = response.body().getData();
                    Collections.reverse(notesList);
                    mAdapterNotes.setRecyclerViewNotesList(notesList);

                } else {
                    Toast.makeText(getBaseCallNoteActivity(), "Unable to fetch latest notes right now. Please try later.", Toast.LENGTH_SHORT).show();
                    getBaseCallNoteActivity().dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Note>>> call, Throwable t) {
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
}
