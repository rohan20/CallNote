package com.rohan.callnote;


import android.os.Bundle;
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
public class NotesFragment extends BaseCallNoteFragment {

    @BindView(R.id.test_text_view)
    TextView testTextView;


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


        Bundle b = getArguments();
        if (b != null && b.getString("email") != null) {
            testTextView.setText(b.getString("email"));
        }

        return v;
    }

}
