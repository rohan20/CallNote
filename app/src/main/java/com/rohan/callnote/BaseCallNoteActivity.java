package com.rohan.callnote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseCallNoteActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static int GOOGLE_SIGN_IN = 1;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        switchFragment(new LoginFragment(), LoginFragment.class.getSimpleName());
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            GoogleSignInAccount acct = result.getSignInAccount();

            Bundle bundle = new Bundle();
            if (acct != null)
                bundle.putString("email", acct.getEmail());
            switchFragment(new NotesFragment(), false, bundle, NotesFragment.class.getSimpleName());

        } else {
            Toast.makeText(this, "failed error code: " + result.getStatus().getStatusMessage() + "\n" + result.getStatus().getStatusCode(), Toast.LENGTH_SHORT).show();
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
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Toast.makeText(BaseCallNoteActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                        switchFragment(new LoginFragment(), LoginFragment.class.getSimpleName());
                    }
                }
        );
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }
}
