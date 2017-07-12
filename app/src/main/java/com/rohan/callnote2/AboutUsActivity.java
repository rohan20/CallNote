package com.rohan.callnote2;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class AboutUsActivity extends AppCompatActivity {

    RelativeLayout mParentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mParentLayout = (RelativeLayout) findViewById(R.id.about_us_parent_layout);
        showSnackbar(getString(R.string.view_app_on_playstore));
    }

    public void showSnackbar(String snackbarText) {
        Snackbar.make(mParentLayout, snackbarText, Snackbar.LENGTH_LONG).show();
    }

}
