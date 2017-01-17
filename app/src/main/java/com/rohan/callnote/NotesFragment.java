package com.rohan.callnote;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends BaseCallNoteFragment implements View.OnClickListener {

    @BindView(R.id.test_text_view)
    TextView testTextView;

    @BindView(R.id.add_call_fab)
    FloatingActionButton mAddCallFAB;

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

        mAddCallFAB.setOnClickListener(this);

        Bundle b = getArguments();
        if (b != null && b.getString("email") != null) {
            testTextView.setText(b.getString("email"));
        }

        return v;
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
