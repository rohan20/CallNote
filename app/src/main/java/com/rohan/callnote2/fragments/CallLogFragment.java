package com.rohan.callnote2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohan.callnote2.BaseCallNoteFragment;
import com.rohan.callnote2.R;
import com.rohan.callnote2.adapters.CallLogAdapter;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallLogFragment extends BaseCallNoteFragment {

    @BindView(R.id.call_log_recycler_view)
    RecyclerView mCallLogRecyclerView;

    List<Call> callsList;

    public CallLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_call_log, container, false);
        ButterKnife.bind(this, v);

        CallsProvider callsProvider = new CallsProvider(getBaseCallNoteActivity());
        callsList = callsProvider.getCalls().getList();
        Collections.reverse(callsList);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getBaseCallNoteActivity(), LinearLayoutManager.VERTICAL, false);
        CallLogAdapter mAdapterCallLog = new CallLogAdapter(getBaseCallNoteActivity());
        mCallLogRecyclerView.setAdapter(mAdapterCallLog);
        mCallLogRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapterCallLog.setRecyclerViewCallLogList(callsList);

        return v;
    }

}
