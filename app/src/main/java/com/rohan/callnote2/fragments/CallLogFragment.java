package com.rohan.callnote2.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohan.callnote2.BaseCallNoteFragment;
import com.rohan.callnote2.R;
import com.rohan.callnote2.adapters.CallLogAdapter;
import com.rohan.callnote2.utils.Constants;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;

import static android.Manifest.permission.READ_CALL_LOG;


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

        if (ContextCompat.checkSelfPermission(getBaseCallNoteActivity(),
                READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},
                    Constants.MY_PERMISSIONS_REQUEST_READ_CALL_LOG_FAB);
        } else {
            setupCallLogData();
        }

        return v;
    }

    private void setupCallLogData() {
        CallsProvider callsProvider = new CallsProvider(getBaseCallNoteActivity());
        callsList = callsProvider.getCalls().getList();
        Collections.reverse(callsList);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager
                (getBaseCallNoteActivity(), LinearLayoutManager.VERTICAL, false);
        CallLogAdapter mAdapterCallLog = new CallLogAdapter(getBaseCallNoteActivity());
        mCallLogRecyclerView.setAdapter(mAdapterCallLog);
        mCallLogRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapterCallLog.setRecyclerViewCallLogList(callsList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constants.MY_PERMISSIONS_REQUEST_READ_CALL_LOG_FAB: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    setupCallLogData();
                    break;

                } else {
                    getBaseCallNoteActivity()
                            .showSnackbar(getString(R.string.permission_required_call_log));
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other permissions this app might request
        }

    }


}
