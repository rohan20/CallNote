package com.rohan.callnote;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends BaseCallNoteFragment {

    @BindView(R.id.add_note_call_name_text_view)
    TextView mCallNameTextView;
    @BindView(R.id.add_note_call_number_text_view)
    TextView mCallNumberTextView;
    @BindView(R.id.add_note_edit_text)
    EditText mNoteEditText;
    @BindView(R.id.add_note_call_date_text_view)
    TextView mCallDateTextView;
    @BindView(R.id.add_note_call_type_image_view)
    ImageView mCallTypeImageView;

    @BindView(R.id.add_note_save_button)
    Button mSaveButton;
    @BindView(R.id.add_note_cancel_button)
    Button mCancelButton;

    public AddNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_note, container, false);
        ButterKnife.bind(this, v);

        mNoteEditText.requestFocus();
        Bundle b = getArguments();

        InputMethodManager imm = (InputMethodManager) getBaseCallNoteActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //set caller name
        if (b.getString("name") == null) {
            mCallNameTextView.setText(b.getString("number"));
        } else {
            mCallNameTextView.setText(b.getString("name"));
        }

        //set call type
        int callType = b.getInt("callType");
        if (callType == 0) {
            mCallTypeImageView.setImageResource(R.drawable.ic_call_missed_red_300_18dp);
        } else if (callType == 1) {
            mCallTypeImageView.setImageResource(R.drawable.ic_call_received_blue_300_18dp);
        } else {
            mCallTypeImageView.setImageResource(R.drawable.ic_call_made_green_300_18dp);
        }

        //set call number
        mCallNumberTextView.setText(b.getString("number"));

        //set call date
        Date date = new Date(b.getLong("date"));
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, HH:mm");
        String dateString = formatter.format(date);
        mCallDateTextView.setText(dateString);

        return v;
    }

    @OnClick(R.id.add_note_save_button)
    public void saveButtonClicked() {
        // TODO: 17-Jan-17 Call add_notes API

        Toast.makeText(getBaseCallNoteActivity(), "Saved", Toast.LENGTH_SHORT).show();
        getBaseCallNoteActivity().switchFragment(new NotesFragment(), NotesFragment.class.getSimpleName());
    }

    @OnClick(R.id.add_note_cancel_button)
    public void cancelButtonClicked() {
        Toast.makeText(getBaseCallNoteActivity(), "Cancel", Toast.LENGTH_SHORT).show();
        getBaseCallNoteActivity().switchFragment(new NotesFragment(), NotesFragment.class.getSimpleName());
    }
}
