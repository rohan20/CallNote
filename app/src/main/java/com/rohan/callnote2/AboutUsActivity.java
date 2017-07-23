package com.rohan.callnote2;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rohan.callnote2.adapters.AppsAdapter;
import com.rohan.callnote2.models.App;
import com.rohan.callnote2.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout mParentLayout;
    RecyclerView mAppsRecyclerView;
    AppsAdapter mAppsAdapter;

    ImageView mFacebookImageView;
    ImageView mLinkedInImageView;
    ImageView mGmailImageView;
    ImageView mGithubImageView;
    ImageView mQuoraImageView;

    String mURL;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mParentLayout = (RelativeLayout) findViewById(R.id.about_us_parent_layout);
        mAppsRecyclerView = (RecyclerView) findViewById(R.id.apps_recycler_view);
        showSnackbar(getString(R.string.view_app_on_playstore));

        mAppsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAppsAdapter = new AppsAdapter(this, getAppsList());
        mAppsRecyclerView.setAdapter(mAppsAdapter);

        mFacebookImageView = (ImageView) findViewById(R.id.facebook_icon_image_view);
        mLinkedInImageView = (ImageView) findViewById(R.id.linkedin_icon_image_view);
        mGmailImageView = (ImageView) findViewById(R.id.gmail_icon_image_view);
        mGithubImageView = (ImageView) findViewById(R.id.github_icon_image_view);
        mQuoraImageView = (ImageView) findViewById(R.id.quora_icon_image_view);

        Picasso.with(this).load(Constants.FACEBOOK_ICON)
                .placeholder(R.drawable.placeholder_no_image_available)
                .into(mFacebookImageView);
        Picasso.with(this).load(Constants.LINKEDIN_ICON)
                .placeholder(R.drawable.placeholder_no_image_available)
                .into(mLinkedInImageView);
        Picasso.with(this).load(Constants.GMAIL_ICON)
                .placeholder(R.drawable.placeholder_no_image_available)
                .into(mGmailImageView);
        Picasso.with(this).load(Constants.QUORA_ICON)
                .placeholder(R.drawable.placeholder_no_image_available)
                .into(mQuoraImageView);
        Picasso.with(this).load(Constants.GITHUB_ICON)
                .placeholder(R.drawable.placeholder_no_image_available)
                .into(mGithubImageView);

        mFacebookImageView.setOnClickListener(this);
        mLinkedInImageView.setOnClickListener(this);
        mGmailImageView.setOnClickListener(this);
        mGithubImageView.setOnClickListener(this);
        mQuoraImageView.setOnClickListener(this);

        mURL = Constants.MY_LINKEDIN_URL;
        i = new Intent(Intent.ACTION_VIEW);
    }

    private List<App> getAppsList() {

        List<App> appsList = new ArrayList<>();

        appsList.add(new App("Movie Roll", "This app allows you to check out the top rated and " +
                "most popular apps using TMDB API", "http://i.imgur.com/T5Q7izs.png",
                "https://play.google.com/store/apps/details?id=com.rohan.movieroll"));
        appsList.add(new App("Balloon Popper", "Easy to play game where you get pop as many balloons" +
                " as you can buy just touching them. The catch? You get only 5 lives!",
                "http://i.imgur.com/w5GY0yX.png", "https://play.google" +
                ".com/store/apps/details?id=com.rohan.balloongame"));

        return appsList;
    }

    public void showSnackbar(String snackbarText) {
        Snackbar.make(mParentLayout, snackbarText, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.facebook_icon_image_view:
                mURL = Constants.MY_FACEBOOK_URL;
                break;

            case R.id.linkedin_icon_image_view:
                mURL = Constants.MY_LINKEDIN_URL;
                break;

            case R.id.quora_icon_image_view:
                mURL = Constants.MY_QUORA_URL;
                break;

            case R.id.github_icon_image_view:
                mURL = Constants.MY_GITHUB_URL;
                break;

            case R.id.gmail_icon_image_view:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + Constants.MY_GMAIL_EMAIL));
                startActivity(emailIntent);
                return;

        }

        i.setData(Uri.parse(mURL));
        startActivity(i);

    }
}
