package com.rohan.callnote;


import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseCallNoteFragment extends Fragment {


    public BaseCallNoteFragment() {
        // Required empty public constructor
    }

    /**
     * This method returns parent Activity
     *
     * @return Object of OnBoardingActivity
     */
    public BaseCallNoteActivity getBaseCallNoteActivity() {
        return (BaseCallNoteActivity) getActivity();
    }

}
