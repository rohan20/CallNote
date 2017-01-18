package com.rohan.callnote;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.rohan.callnote.models.User;
import com.rohan.callnote.network.APIClient;
import com.rohan.callnote.network.response.ApiResponse;
import com.rohan.callnote.utils.Constants;
import com.rohan.callnote.utils.SharedPrefsUtil;
import com.rohan.callnote.utils.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rohan.callnote.utils.SharedPrefsUtil.USER_EMAIL;

public class BaseCallNoteActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    GoogleApiClient mGoogleApiClient;
    private static BaseCallNoteActivity instance;
    private ProgressDialog signInProgressDialog;

    CallStateReceiver callStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        instance = this;
        callStateReceiver = new CallStateReceiver();

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        signInProgressDialog = new ProgressDialog(this);
        signInProgressDialog.setCancelable(true);
        signInProgressDialog.setCanceledOnTouchOutside(false);

        switchFragment(new LoginFragment(), LoginFragment.class.getSimpleName());
    }

    @Override
    protected void onStart() {
        super.onStart();


        Bundle b = new Bundle();
        b.putBoolean(Constants.ADD_NOTE_DIRECTLY_FROM_CALL, false);

        if (getIntent() != null && getIntent().getBooleanExtra(Constants.ADD_NOTE_DIRECTLY_FROM_CALL, false)) {
            //directly go to add note fragment
            b.putBoolean(Constants.ADD_NOTE_DIRECTLY_FROM_CALL, true);
            switchFragment(new LoginFragment(), false, b, LoginFragment.class.getSimpleName());
        }
    }

    public static BaseCallNoteActivity getInstance() {
        return instance;
    }

    public void signIn() {

//        registerReceiver(callStateReceiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));

        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.server_client_id))
                        .requestEmail()
                        .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String token = acct.getIdToken();

            Call<ApiResponse<User>> call = APIClient.getApiService().signUp(name, email, token);
            call.enqueue(new Callback<ApiResponse<User>>() {
                @Override
                public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                    if (response.isSuccessful()) {
                        User user = response.body().getData();
                        UserUtil.saveUser(user);
                        switchFragment(new NotesFragment(), false, NotesFragment.class.getSimpleName());
                        dismissProgressDialog();
                    } else {
                        Toast.makeText(BaseCallNoteActivity.this, "Failed to sign in. Please try later.", Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                    Toast.makeText(BaseCallNoteActivity.this, "Unable to sign in right now. Please try later.", Toast.LENGTH_SHORT).show();
                    dismissProgressDialog();
                }
            });

        } else {
            Toast.makeText(BaseCallNoteActivity.this, "Unable to sign in right now. Please try later.", Toast.LENGTH_SHORT).show();
            dismissProgressDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sign_out:
                signOut();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(SharedPrefsUtil.retrieveStringValue(USER_EMAIL, null))
                .setMessage("Are you sure you want to sign out?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        Toast.makeText(BaseCallNoteActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                                        UserUtil.logout();
                                        switchFragment(new LoginFragment(), LoginFragment.class.getSimpleName());
//                                        unregisterReceiver(callStateReceiver);
                                    }
                                }
                        );
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Method Called to replace the to replace the container i.e. R.id.containerView  with the new fragment
     *
     * @param fragment pass value of the fragment will replace the container
     */
    public void switchFragment(Fragment fragment) {
        switchFragment(fragment, null);
    }

    /**
     * Method Called to replace the to replace the container i.e. R.id.containerView  with the new fragment
     *
     * @param fragment pass value of the fragment will replace the container
     * @param tag
     */
    public void switchFragment(Fragment fragment, String tag) {
        switchFragment(fragment, false, null, tag);
    }

    /**
     * Method Called to replace the to replace the container i.e. R.id.containerView  with the new fragment
     *
     * @param fragment       pass value of the fragment will replace the container
     * @param addToBackStack
     * @param tag
     */
    public void switchFragment(Fragment fragment, boolean addToBackStack, String tag) {
        switchFragment(fragment, addToBackStack, null, tag);
    }

    /**
     * Method Called to replace the to replace the container i.e. R.id.containerView  with the new fragment
     *
     * @param fragment
     * @param addToBackStack
     * @param bundle
     * @param tag
     */
    public void switchFragment(Fragment fragment, boolean addToBackStack, Bundle bundle, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(R.id.containerView, fragment, tag).commit();

    }

    public void showProgressDialog(String message) {
        signInProgressDialog.setMessage(message);
        signInProgressDialog.show();
    }

    public void dismissProgressDialog() {
        signInProgressDialog.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    public int getCallType(me.everything.providers.android.calllog.Call.CallType callType) {
        if (callType.equals(me.everything.providers.android.calllog.Call.CallType.MISSED))
            return Constants.CALL_MISSED;
        else if (callType.equals(me.everything.providers.android.calllog.Call.CallType.INCOMING))
            return Constants.CALL_RECEIVED;
        else
            return Constants.CALL_DIALED;
    }
}
