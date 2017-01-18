package com.rohan.callnote;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.rohan.callnote.utils.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseCallNoteFragment {

    @BindView(R.id.google_sign_in_button)
    SignInButton mGoogleSignInButton;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (UserUtil.isUserLoggedIn()) {
            skipLogin();
            return null;
        }

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        getBaseCallNoteActivity().getSupportActionBar().hide();
        mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);
        mGoogleSignInButton.setSize(SignInButton.COLOR_LIGHT);
        return v;
    }

    @OnClick(R.id.google_sign_in_button)
    public void signIn() {

        getBaseCallNoteActivity().showProgressDialog("Signing in...");

        if (getBaseCallNoteActivity().isNetworkConnected()) {
            getBaseCallNoteActivity().signIn();
        } else {
            Toast.makeText(getBaseCallNoteActivity(), "Please connect to internet.", Toast.LENGTH_LONG).show();
        }
    }

    private void skipLogin() {
        getBaseCallNoteActivity().switchFragment(new NotesFragment(), false, NotesFragment.class.getSimpleName());
    }

}
