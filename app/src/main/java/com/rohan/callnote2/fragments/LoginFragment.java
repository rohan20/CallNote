package com.rohan.callnote2.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.SignInButton;
import com.rohan.callnote2.BaseCallNoteFragment;
import com.rohan.callnote2.R;
import com.rohan.callnote2.utils.Constants;
import com.rohan.callnote2.utils.UserUtil;

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

        getBaseCallNoteActivity().showProgressDialog(getString(R.string.signing_in));

        if (getBaseCallNoteActivity().isNetworkConnected()) {
            getBaseCallNoteActivity().signIn();
        } else {
            getBaseCallNoteActivity().showSnackbar(getString(R.string.please_connect_to_the_internet_error));
            getBaseCallNoteActivity().dismissProgressDialog();
        }
    }

    private void skipLogin() {
        Bundle b = getArguments();

        if (b != null && b.getBoolean(Constants.ADD_NOTE_DIRECTLY_FROM_CALL)) {

            CallsProvider callsProvider = new CallsProvider(getBaseCallNoteActivity());
            List<Call> callsList = callsProvider.getCalls().getList();

            Call call = callsList.get(callsList.size() - 1);

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.name_key), call.name);
            bundle.putString(getString(R.string.number_key), call.number);
            bundle.putInt(getString(R.string.callType_key), getBaseCallNoteActivity().getCallType
                    (call.type));
            bundle.putLong(getString(R.string.date_key), call.callDate);
            bundle.putBoolean(Constants.ADD_NOTE_DIRECTLY_FROM_CALL, true);

            getBaseCallNoteActivity().switchFragment(new AddNoteFragment(), false, bundle, AddNoteFragment.class.getSimpleName());
        } else {
            getBaseCallNoteActivity().switchFragment(new NotesFragment(), false, NotesFragment.class.getSimpleName());
        }
    }

}
