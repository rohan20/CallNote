package com.rohan.callnote2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.rohan.callnote2.fragments.LoginFragment;
import com.rohan.callnote2.fragments.NotesFragment;
import com.rohan.callnote2.models.User;
import com.rohan.callnote2.network.ApiClient;
import com.rohan.callnote2.network.ApiResponse;
import com.rohan.callnote2.utils.Constants;
import com.rohan.callnote2.utils.SharedPrefsUtil;
import com.rohan.callnote2.utils.UserUtil;
import com.rohan.callnote2.widget.CallNoteWidget;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rohan.callnote2.utils.SharedPrefsUtil.USER_EMAIL;

public class BaseCallNoteActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    static final String TAG = BaseCallNoteActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_base_relative_layout)
    RelativeLayout mParentLayout;

    GoogleApiClient mGoogleApiClient;
    public static BaseCallNoteActivity instance;
    private ProgressDialog signInProgressDialog;
    private ProgressDialog fetchingNotesProgressDialog;

    public FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Log.e("onn", "onCreate()");
        instance = this;

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        signInProgressDialog = new ProgressDialog(this);
        signInProgressDialog.setCancelable(true);
        signInProgressDialog.setCanceledOnTouchOutside(false);

        fetchingNotesProgressDialog = new ProgressDialog(this);
        fetchingNotesProgressDialog.setCancelable(true);
        fetchingNotesProgressDialog.setCanceledOnTouchOutside(false);


        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.server_client_id))
                        .requestEmail()
                        .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(BaseCallNoteActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        switchFragment(new LoginFragment(), LoginFragment.class.getSimpleName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onn", "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onn", "onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("onn", "onStart()");

        Bundle b = new Bundle();
        b.putBoolean(Constants.ADD_NOTE_DIRECTLY_FROM_CALL, false);

        if (getIntent() != null && getIntent().getBooleanExtra(Constants.ADD_NOTE_DIRECTLY_FROM_CALL, false)) {
            //directly go to add note fragment
            b.putBoolean(Constants.ADD_NOTE_DIRECTLY_FROM_CALL, true);
            switchFragment(new LoginFragment(), false, b, LoginFragment.class.getSimpleName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onn", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onn", "onStop()");
    }

    public static BaseCallNoteActivity getInstance() {
        return instance;
    }

    public void signIn() {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String token = acct.getIdToken();

            Call<ApiResponse<User>> call = ApiClient.getApiService().signUp(name, email, token);
            call.enqueue(new Callback<ApiResponse<User>>() {
                @Override
                public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                    if (response.isSuccessful()) {
                        User user = response.body().getData();
                        UserUtil.saveUser(user);
                        switchFragment(new NotesFragment(), false, NotesFragment.class.getSimpleName());
                        dismissProgressDialog();
                    } else {
                        showSnackbar(getString(R.string.failed_to_sign_in_error));
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                    Log.e("status", t.getMessage());
                    showSnackbar(getString(R.string.unable_to_sign_in_error));
                    dismissProgressDialog();
                }
            });

        } else {
            Log.e("status result", result.getStatus().toString());
            showSnackbar(getString(R.string.unable_to_sign_in_error));
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

            case R.id.action_about_us:
                Intent i = new Intent(BaseCallNoteActivity.this, AboutUsActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(SharedPrefsUtil.retrieveStringValue(USER_EMAIL, null))
                .setMessage(getString(R.string.sign_out_confirmation))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        showSnackbar(getString(R.string.signed_out));
                                        UserUtil.logout();
                                        switchFragment(new LoginFragment(), LoginFragment.class.getSimpleName());
                                    }
                                }
                        );
                    }
                })
                .setNegativeButton(getString(R.string.cancel_string), new DialogInterface
                        .OnClickListener() {
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
//        fragmentTransaction.replace(R.id.containerView, fragment, tag).commit();
        fragmentTransaction.replace(R.id.containerView, fragment, tag).commitAllowingStateLoss();

    }

    public void showProgressDialog(String message) {
        signInProgressDialog.setMessage(message);
        signInProgressDialog.show();
    }

    public void dismissProgressDialog() {
        signInProgressDialog.dismiss();
    }

    public void showFetchingNotesProgressDialog(String message) {
        fetchingNotesProgressDialog.setMessage(message);
        fetchingNotesProgressDialog.show();
    }

    public void dismissFetchingNotesProgressDialog() {
        fetchingNotesProgressDialog.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public int getCallType(me.everything.providers.android.calllog.Call.CallType callType) {

        if (callType != null) {
            if (callType.equals(me.everything.providers.android.calllog.Call.CallType.MISSED))
                return Constants.CALL_MISSED;
            else if (callType.equals(me.everything.providers.android.calllog.Call.CallType.INCOMING))
                return Constants.CALL_RECEIVED;
        }

        return Constants.CALL_DIALED;
    }

    public void updateWidget() {
        ComponentName name = new ComponentName(this, CallNoteWidget.class);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(name);
        Intent intent = new Intent(this, CallNoteWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    public void showSnackbar(String snackbarText) {
        Snackbar.make(mParentLayout, snackbarText, Snackbar.LENGTH_SHORT).show();
    }

}
