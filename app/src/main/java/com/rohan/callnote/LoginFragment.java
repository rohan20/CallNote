package com.rohan.callnote;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.rohan.callnote.utils.Constants;
import com.rohan.callnote.utils.UserUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;


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
        Bundle b = getArguments();

        if (b != null && b.getBoolean(Constants.ADD_NOTE_DIRECTLY_FROM_CALL)) {

            CallsProvider callsProvider = new CallsProvider(getBaseCallNoteActivity());
            List<Call> callsList = callsProvider.getCalls().getList();

            Call call = callsList.get(callsList.size() - 1);

            Bundle bundle = new Bundle();
            bundle.putString("name", call.name);
            bundle.putString("number", call.number);
            bundle.putInt("callType", getBaseCallNoteActivity().getCallType(call.type));
            bundle.putLong("date", call.callDate);
            bundle.putBoolean(Constants.ADD_NOTE_DIRECTLY_FROM_CALL, true);

            getBaseCallNoteActivity().switchFragment(new AddNoteFragment(), false, bundle, AddNoteFragment.class.getSimpleName());
        } else {
            getBaseCallNoteActivity().switchFragment(new NotesFragment(), false, NotesFragment.class.getSimpleName());
        }
    }

}
